package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.foodtrain.R
import com.example.foodtrain.models.HomeHorModel

class HomeHorAdapter(val context : Context,private val productList: List<HomeHorModel>) : RecyclerView.Adapter<HomeHorAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_horizontal_food_item,
                parent,
                false
            )
        )
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.itemView.apply {
            findViewById<ImageView>(R.id.food_type).setImageResource(product.imageSrc)
            findViewById<TextView>(R.id.food_type_text).text = product.foodType
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }
}