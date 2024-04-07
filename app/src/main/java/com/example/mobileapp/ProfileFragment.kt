package com.example.mobileapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mobileapp.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

private lateinit var bottomNavigationView: BottomNavigationView
private lateinit var binding:FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
val view=binding.root
        auth= FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()

       binding.btnLogout.setOnClickListener {
            if(auth.currentUser!=null)
            {
        auth.signOut()

val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(getString(R.string.default_web_client_id))
    .requestEmail()
    .build()


val googleSignInClient=GoogleSignIn.getClient(requireActivity(),gso)
                googleSignInClient.signOut().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                LoginActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    } else {
                        Log.e(tag, "sign out failed", task.exception)
                    }
                }
       // Inflate the layout for this fragment return inflater.inflate(R.layout.fragment_profile, container, false)
    }}

        binding.btnDeleteAccount.setOnClickListener {


            AlertDialog.Builder(requireContext())
                .setTitle("Delete account")
                .setMessage(" All your data will not be recovered after account deletion. " +
                        "Are you sure you want to delete your account? ")
                .setPositiveButton("yes"){dialog,it->
                    deleteUserAccount()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog,it->
                    dialog.dismiss()
                }
                .show()

        }




        return view
}
private fun deleteUserAccount(){
    val user=auth.currentUser
    user?.delete()
        ?.addOnCompleteListener {
            task->
            if(task.isSuccessful){
                val userId=user.uid
                firestore.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Account deleted successfuuly",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(activity,SignupActivity::class.java))
                        activity?.finish()
                    }
                    .addOnFailureListener{e->
                        Toast.makeText(
                            activity,
                            "Failed to delete user data:${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }else{
                Toast.makeText(
                    activity,
                    "Failed to delete account:${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

}

