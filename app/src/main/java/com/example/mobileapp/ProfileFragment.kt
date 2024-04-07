package com.example.mobileapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileapp.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth



class ProfileFragment : Fragment() {

private lateinit var bottomNavigationView: BottomNavigationView
private lateinit var binding:FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
val view=binding.root
        auth= FirebaseAuth.getInstance()

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
        return view
}}