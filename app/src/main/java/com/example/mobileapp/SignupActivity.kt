package com.example.mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobileapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private  lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            var email=binding.signupEmail.text.toString()
            var password=binding.signupPassword.text.toString()
            var confirmPassword=binding.signupConfirm.text.toString()

            if (email.isNotEmpty()&&password.isNotEmpty()&&confirmPassword.isNotEmpty()){
                if (password==confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            var intent=Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show()
                }

            }
            binding.loginRedirectText.setOnClickListener {
                var loginIntent=Intent(
                    this,LoginActivity::class.java)
                startActivity(loginIntent)
            }

        }
    }
}