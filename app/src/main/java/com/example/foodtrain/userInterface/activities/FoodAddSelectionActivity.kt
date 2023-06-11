package com.example.foodtrain.userInterface.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodtrain.R

class FoodAddSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_add_selection)

        val addFoodTypeButton = findViewById<Button>(R.id.food_type_btn)
        addFoodTypeButton.setOnClickListener {
            startActivity(Intent(this,AddFoodTypeActivity::class.java))
        }
    }
}