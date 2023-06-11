package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.models.FoodItem
import com.example.foodtrain.models.FoodType

class HomeHorAdapter(
    val context : Context,
    private val productList: List<FoodType>,
    private val onFoodTypeSelected : (FoodType) -> Unit

    ) : RecyclerView.Adapter<HomeHorAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_horizontal_food_item,
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){

        private val textView: TextView = itemView.findViewById(R.id.food_type_text)
        private val image :ImageView = itemView.findViewById(R.id.food_type)

        fun bind(foodType: FoodType) {

            textView.text = foodType.foodTypeName
            //image.setImageResource(foodType.foodTypeImage)
            Glide.with(itemView).load(foodType.foodTypeImage).into(image)
            itemView.setOnClickListener {
                onFoodTypeSelected(foodType)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
//        holder.itemView.apply {
//            findViewById<ImageView>(R.id.food_type).setImageResource(product.imageSrc)
//            findViewById<TextView>(R.id.food_type_text).text = product.foodType
//        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
}