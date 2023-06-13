package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.AddToCart
import com.example.foodtrain.models.FoodItem
import com.google.type.DateTime
import kotlinx.android.synthetic.main.home_vertical_food_item_type.view.*
import java.util.*

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

        holder.itemView.addToCartImage.setOnClickListener {
            val orderId = generateNewID()
            val foodId = food.food_id
            val foodName = food.food_name
            val quantity = 1
            val totalPrice = food.food_price
            val foodImage = food.food_image

            val addToCart = AddToCart(
                orderId,
                "",
                foodId,
                foodImage,
                foodName,
                quantity,
                totalPrice,
            )

            FireStoreClass().addToCartOrdersToFireStore(context,addToCart)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.food_item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceOfaFood)
        private val image: ImageView = itemView.findViewById(R.id.food_image)

        fun bind(food: FoodItem) {
            nameTextView.text = food.food_name
            priceTextView.text = "LKR ${food.food_price.toString()}"
            //Glide.with(itemView).load(food.food_image).into(image)
            GlideLoader(itemView.context).loadUserPicture(food.food_image.toUri(),image)
        }

    }
    override fun getItemCount(): Int {
        return foods.size
    }
    private fun generateNewID() : String{
        return UUID.randomUUID().toString()
    }
}