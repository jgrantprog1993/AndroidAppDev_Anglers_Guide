package ie.wit.anglersguide.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseAuth
import ie.wit.anglersguide.databinding.ActivityAllmarkersmapBinding.inflate
import ie.wit.anglersguide.databinding.NavHeaderBinding.inflate
import ie.wit.anglersguide.databinding.HostingAccActivityBinding
import timber.log.Timber.i


class HostingAccActivity : AppCompatActivity() {

    private lateinit var hostingAccActivityBinding: HostingAccActivityBinding


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        //setContentView(R.layout.content_home)
        hostingAccActivityBinding = HostingAccActivityBinding.inflate(layoutInflater)
        setContentView(hostingAccActivityBinding.root)

        initData()
    }



    private fun initData(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn(){
        val currentUser = auth.currentUser
        i(" auth.currentUser - ${auth.currentUser}")
        if (currentUser != null)
        {
            startActivity(Intent(this, Home::class.java))
            finish()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}

