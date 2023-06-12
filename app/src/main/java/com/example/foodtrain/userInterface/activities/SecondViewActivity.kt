package com.example.foodtrain.userInterface.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodtrain.R

class SecondViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_view)

        val loginLink = findViewById<Button>(R.id.loginLink)
        val signUpLink = findViewById<Button>(R.id.SingUpLink)

        loginLink.setOnClickListener {
            //startActivity(Intent(this@SecondViewActivity, LoginActivity::class.java))
            startActivity(Intent(this@SecondViewActivity, BottomNavBarActivity::class.java))
            finish()
        }
        signUpLink.setOnClickListener {
            startActivity(Intent(this@SecondViewActivity, RegisterActivity::class.java))
            finish()
        }
    }
}