//package ie.wit.anglersguide.ui.splash
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import ie.wit.anglersguide.R
//import ie.wit.anglersguide.models.SplashModel
//import ie.wit.anglersguide.ui.auth.Login
//import ie.wit.anglersguide.ui.home.Home
//
//class SplashActivity : AppCompatActivity() {
//
//    private lateinit var splashViewModel: SplashViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.splash_screen)
//        initViewModel()
//        //observeSplashLiveData()
//    }
//
//    private fun initViewModel() {
//        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
//    }
//
//    private fun observeSplashLiveData() {
//        splashViewModel.initSplashScreen()
//
//            val intent = Intent(this@SplashActivity, Home::class.java)
//            startActivity(intent)
//            finish()
//
//    }
//}
