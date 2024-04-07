package com.example.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem->
           when(menuItem.itemId){
               R.id.bottom_home->{
                   replaceFragment(HomeFragment())
                   true

               }
               R.id.bottom_profile->{
                   replaceFragment(ProfileFragment())
                   true
               }
               R.id.bottom_settings->{
                   replaceFragment(SettingsFragment())
                   true
               }
               else->false
           }
        }
        replaceFragment(HomeFragment())



    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }
}