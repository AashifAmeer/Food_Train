package com.example.foodtrain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtrain.Constants
import com.example.foodtrain.GlideLoader
import com.example.foodtrain.R
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.AddToCart
import kotlinx.android.synthetic.main.order_vertical_view.view.*


class OrderVerAdapter(
    val context: Context,
    private val orderDetails: List<AddToCart>
) : RecyclerView.Adapter<OrderVerAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val foodName: TextView = itemView.findViewById(R.id.order_name)
        private val foodPrice: TextView = itemView.findViewById(R.id.product_price)
        private val foodImage: ImageView = itemView.findViewById(R.id.food_order_image)
        private val incrementBtn: Button = itemView.findViewById(R.id.increment_button)
        private val decrementBtn: Button = itemView.findViewById(R.id.decrement_button)
        private val quantityText: TextView = itemView.findViewById(R.id.quantityChange)

        private var increment_decrement_count = 1

        fun bind(orders: AddToCart) {
            foodName.text = orders.food_name
            foodPrice.text = "LKR ${orders.food_total_price}"
            GlideLoader(itemView.context).loadUserPicture(orders.food_image.toUri(), foodImage)

            quantityText.text = increment_decrement_count.toString()

            incrementBtn.setOnClickListener {
                increment_decrement_count++
                val totalPrice = increment_decrement_count * orders.food_total_price
                updateQuantityAndPrice(totalPrice,orders.order_id)
            }

            decrementBtn.setOnClickListener {
                if (increment_decrement_count > 1) {
                    increment_decrement_count--
                    val totalPrice = increment_decrement_count * orders.food_total_price
                    updateQuantityAndPrice(totalPrice,orders.order_id)
                }
            }

        }

        private fun updateQuantityAndPrice(totalPrice: Double,orderId : String) {
            quantityText.text = increment_decrement_count.toString()
            foodPrice.text = "LKR $totalPrice"

            val orderHashMap = HashMap<String,Any>()
            orderHashMap[Constants.FOOD_QUANTITY_ORDER] = increment_decrement_count
            orderHashMap[Constants.FOOD_TOTAL_PRICE] = foodPrice.text.toString().trim(' ','L','K','R').toDouble()
            FireStoreClass().updateOrderDetails(orderId,orderHashMap)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_vertical_view, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orders = orderDetails[position]
        holder.bind(orders)
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }
}

