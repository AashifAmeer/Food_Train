package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtrain.R
import com.example.foodtrain.models.FoodItem

class HomeVerFoodListAdapter(
    val context : Context,private val foods : List<FoodItem>
    ) :
    RecyclerView.Adapter<HomeVerFoodListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_vertical_food_item_type, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.food_item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceOfaFood)
        private val image: ImageView =
            itemView.findViewById(R.id.food_image)

        fun bind(food: FoodItem) {
            nameTextView.text = food.food_name
            priceTextView.text = food.food_price.toString()
            //image.setImageResource(food.image)
        }

    }
    override fun getItemCount(): Int {
        return foods.size
    }
}