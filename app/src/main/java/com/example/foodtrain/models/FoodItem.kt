package com.example.foodtrain.models

data class FoodItem (
    val food_id : String = "",
    val food_name : String = "",
    val food_image :String = "",
    val food_price : Double = 0.0,
    val food_quantity : Int = 0,
    val food_description : String = "",
    val food_type_id : String = ""
        )