package com.example.foodtrain.userInterface.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodtrain.Constants
import com.example.foodtrain.R
import com.example.foodtrain.databinding.ActivityBottomNavBarBinding
import com.example.foodtrain.models.User

class BottomNavBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav_bar)

        navView.setupWithNavController(navController)

        val welcomeText = findViewById<TextView>(R.id.userName)

        val sharedPreferences = getSharedPreferences(Constants.FOODTRAIN_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"A").toString()

        welcomeText.text = "Hello, $username"

        //---------------------------------------------------
        var userDetails = User()

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            // Getting extra user details from parcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        val currentUser = findViewById<ImageView>(R.id.currentUser)
        currentUser.setOnClickListener{
            val intent = Intent(this@BottomNavBarActivity, UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,userDetails)
            startActivity(intent)
        }
    }
}