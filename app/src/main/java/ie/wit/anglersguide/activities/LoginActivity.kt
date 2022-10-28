package ie.wit.anglersguide.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import ie.wit.anglersguide.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initData()
    }

    private fun initData(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        clickListener()
    }

    private fun clickListener(){
        binding.llNewUser.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            getUserData()
        }

    }

    private fun getUserData(){
        var email = binding.editTextEmailAddress.text.toString()
        var password = binding.editTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            authUser(email, password)
        } else {
            Toast.makeText(this, "All inputs required... ", Toast.LENGTH_LONG).show()
        }
    }

    private fun authUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                checkResult(it.isSuccessful)
            }
    }

    private fun checkResult(isSuccess: Boolean){
        if(isSuccess)
        {
            startActivity(Intent(this, Home::class.java ))
            finish()
        }else{
            Toast.makeText(this, "Authentication failed . . .", Toast.LENGTH_LONG).show()
        }
    }
}