package com.example.mobileapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobileapp.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

class SignupActivity : AppCompatActivity() {

    private  lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
private lateinit var googleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            googleSignIn()
        }

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

        }
        binding.loginRedirectText.setOnClickListener {
            var loginIntent=Intent(
                this,LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }

    private fun googleSignIn() {

        val signInClient=googleSignInClient.signInIntent
        launcher.launch(signInClient)

    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode==Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
        manageResults(task)
        }
    }

    private fun manageResults(task: Task<GoogleSignInAccount>) {
val account:GoogleSignInAccount?=task.result
        if(account!=null){
            val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(task.isSuccessful){

              val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this,"Account created",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, task.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
else{
        Toast.makeText(this, task.exception.toString(),Toast.LENGTH_SHORT).show()
}

}}