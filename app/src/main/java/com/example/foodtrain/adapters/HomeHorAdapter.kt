package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.models.FoodItem
import com.example.foodtrain.models.FoodType
import kotlinx.android.synthetic.main.home_horizontal_food_item.view.*

class HomeHorAdapter(
    val context : Context,
    private val productList: List<FoodType>,
    private val onFoodTypeSelected : (FoodType) -> Unit

    ) : RecyclerView.Adapter<HomeHorAdapter.MyViewHolder>() {

    private var selectedPosition = 0

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
        private val horCardView :CardView = itemView.findViewById(R.id.horCardView)

        fun bind(foodType: FoodType) {

            textView.text = foodType.foodTypeName

            val backgroundColor = if (adapterPosition == selectedPosition) {
                ContextCompat.getColor(context,R.color.green)
            } else {
                ContextCompat.getColor(context, R.color.red)
            }

            horCardView.setCardBackgroundColor(backgroundColor)

            //image.setImageResource(foodType.foodTypeImage)
            //Glide.with(itemView).load(foodType.foodTypeImage).into(image)
            GlideLoader(itemView.context).loadUserPicture(foodType.foodTypeImage.toUri(),image)
            itemView.setOnClickListener {
                var previousSelectedPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)
                notifyDataSetChanged()
                onFoodTypeSelected(foodType)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }
    override fun getItemCount(): Int {
        return productList.size
    }
}