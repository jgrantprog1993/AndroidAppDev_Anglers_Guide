package ie.wit.anglersguide.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ie.wit.anglersguide.R

class SplashScreenActivity : AppCompatActivity() {

    lateinit var  handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed(
            {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }, 2000)

    }
}