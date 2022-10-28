package ie.wit.anglersguide.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ie.wit.anglersguide.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initData()
    }

    private fun initData(){
        auth = FirebaseAuth.getInstance()
        clickListener()
    }

    private fun clickListener(){
        binding.llOldUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.regButton.setOnClickListener {
            createUser()
        }

    }

    private fun createUser(){
        var email = binding.regEditTextEmailAddress.text.toString()
        var password = binding.regEditTextTextPassword.text.toString()
        var cPassword = binding.regEditTextConfPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty() && cPassword.isNotEmpty()){
            //save user details

            if(password == cPassword){
                saveUser(email,password)
            }else{
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this, "All inputs required", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                checkResults(it.isSuccessful)
            }
    }

    private fun checkResults(isSuccess: Boolean){
        if(isSuccess){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            Toast.makeText(this, "Create an account", Toast.LENGTH_LONG).show()
        }
    }

}
