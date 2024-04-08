package com.example.mobileapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private  lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
private lateinit var googleSignInClient:GoogleSignInClient
private lateinit var firestore:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            googleSignIn()
        }
//save signup user data
        binding.signupButton.setOnClickListener {
            var username=binding.signupUername.text.toString()
            var email=binding.signupEmail.text.toString()
            var password=binding.signupPassword.text.toString()
            var confirmPassword=binding.signupConfirm.text.toString()

            if (email.isNotEmpty()&&password.isNotEmpty()&&confirmPassword.isNotEmpty()&&username.isNotEmpty()){
                if (password==confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                       if(it.isSuccessful){
                           val currentUser=firebaseAuth.currentUser
                           val userId=currentUser?.uid?:""
                           val user= hashMapOf(
                               "username" to username,
                               "email" to email,
                           )

                           firestore.collection("users").document(userId)
                               .set(user)
                               .addOnSuccessListener {
                                   Toast.makeText(this@SignupActivity,"Sign up successfully",Toast.LENGTH_SHORT).show()
                           startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                              }
                               .addOnFailureListener{e->
                                   val errorMessage="Failed to save user data:${e.message}"
                                   Log.e(TAG,errorMessage)
                                   Toast.makeText(this@SignupActivity,errorMessage,Toast.LENGTH_SHORT).show()

                               }
                       }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()
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