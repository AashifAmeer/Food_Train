package com.example.foodtrain.models

data class HomeHorModel(
    val main_food_id : String ="",
    val foodType: String = "",
    val imageSrc : Int = 0,
    var foods : List<FoodVertical> = emptyList()
    )