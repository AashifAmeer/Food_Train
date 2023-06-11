package com.example.foodtrain.models

data class FoodType (
    val foodTypeId : String = "",
    val foodTypeName : String = "",
    val foodTypeImage : String = "",
    val foodItems : List<FoodItem> = emptyList()
        )