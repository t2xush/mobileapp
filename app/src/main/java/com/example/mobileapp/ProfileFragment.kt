package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileapp.databinding.FragmentProfileBinding
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

       requireActivity().startActivity(Intent(requireContext(),LoginActivity::class.java))
      requireActivity().finish()   // Inflate the layout for this fragment return inflater.inflate(R.layout.fragment_profile, container, false)
    }}
        return view
}}