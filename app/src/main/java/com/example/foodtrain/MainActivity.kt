package com.example.foodtrain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        Handler().postDelayed({
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            finish()
//        }, 4500)
        val getStartedButton = findViewById<Button>(R.id.getStartButton)
        getStartedButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,SecondViewActivity::class.java))
            finish()
        }

    }
}
