package ie.wit.anglersguide.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.HomeBinding
import ie.wit.anglersguide.databinding.NavHeaderBinding
import timber.log.Timber.i

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderBinding: NavHeaderBinding

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.content_home)
        homeBinding = HomeBinding.inflate(layoutInflater)
        navHeaderBinding = NavHeaderBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        val navView = homeBinding.navView

        appBarConfiguration = AppBarConfiguration(setOf(
          R.id.fishingSpotListFragment, R.id.mapAllFishingSpotsFragment, R.id.fishingSpotFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        clickListener()
        initData()

    }

    private fun clickListener() {
        homeBinding.logoutbutton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            auth.signOut()
            finish()

        }
    }

    private fun initData(){
        auth = FirebaseAuth.getInstance()
        setUserEmail()
        //auth.signOut()
        //checkIfUserIsLoggedIn()
    }

    private fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    private fun setUserEmail(){
        i("getCurrentUserEmail()" + getCurrentUserEmail())
        navHeaderBinding.loggedInUserGreeting.text = "Hi +"+getCurrentUserEmail()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}