package com.example.foodtrain.models

import com.google.type.DateTime

data class AddToCart (
    val order_id : String = "",
    var user_id : String = "",
    val food_id : String = "",
    val food_image : String = "",
    val food_name : String = "",
    var food_quantity_order: Int = 1,
    var food_total_price : Double = 0.0,
        )