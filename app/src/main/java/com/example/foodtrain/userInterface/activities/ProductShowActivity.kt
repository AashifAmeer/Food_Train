package com.example.foodtrain.userInterface.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.foodtrain.Constants
import com.example.foodtrain.R

class ProductShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_show)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)

        val sharedPreferences = getSharedPreferences(Constants.FOODTRAIN_PREFERENCES,Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"A").toString()

        welcomeText.text = "Hello $username"
    }
}