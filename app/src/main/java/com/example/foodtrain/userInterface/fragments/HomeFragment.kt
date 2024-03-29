package com.example.foodtrain.userInterface.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.foodtrain.R
import com.example.foodtrain.adapters.HomeHorAdapter
import com.example.foodtrain.adapters.HomeVerFoodListAdapter
import com.example.foodtrain.databinding.FragmentHomeBinding
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.FoodItem
import com.example.foodtrain.models.FoodType
import com.example.foodtrain.userInterface.activities.BottomNavBarActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var productList = ArrayList<FoodType>()
    private lateinit var recyclerViewHor : RecyclerView
    private lateinit var recyclerViewVer : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerViewHor = root.findViewById(R.id.food_type_recycler)
        recyclerViewVer = root.findViewById(R.id.food_list_vertical_recycler)
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout)


        productList.clear()
        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = true
                val foodTypes = FireStoreClass().loadFoodTypes()
                productList.clear()
                productList.addAll(foodTypes)
                recyclerViewHor.adapter?.notifyDataSetChanged()
                recyclerViewVer.adapter?.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            swipeRefreshLayout.isRefreshing = true
            val foodTypes = FireStoreClass().loadFoodTypes()
            productList.clear()
            productList.addAll(foodTypes)
            recyclerViewHor.adapter?.notifyDataSetChanged()
            recyclerViewVer.adapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        // Set up RecyclerView vertical


//        val textView: TextView = binding.textHome
//        textView.text = "Home"
        return root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView horizontal
        recyclerViewHor.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        recyclerViewHor.isNestedScrollingEnabled = true

        val adapter = HomeHorAdapter(requireContext(),productList){foodType ->

            showTypeOfFood(foodType.foodItems)
        }
        recyclerViewHor.adapter = adapter



        // Set up RecyclerView vertical
       // recyclerViewVer.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        recyclerViewVer.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)


        // Add sample products
        //addSampleProducts()
    }
    private fun showTypeOfFood(foods: List<FoodItem>) {
        val foodAdapter = HomeVerFoodListAdapter(requireContext(),foods)
        recyclerViewVer.adapter = foodAdapter
    }
//
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getFoodTypes(foodType : ArrayList<FoodType>){
        productList.clear()
        productList.addAll(foodType)
    }



    // Without database

//    private fun addSampleProducts() {
//        productList.add(HomeHorModel("1","Pizza", R.drawable.ic_baseline_local_pizza_24, listOf(
//            FoodVertical("1","Cheese Pizza",R.drawable.cheese_pizza_removebg_preview,"LKR 350"),
//            FoodVertical("2","Chicken Pizza",R.drawable.cheese_pizza_removebg_preview,"LKR 450"),
//            FoodVertical("3","Normal Pizza",R.drawable.regular_pizza_removebg_preview,"LKR 150"),
//            FoodVertical("4","Veg Pizza",R.drawable.veg_pizza_removebg_preview,"LKR 400"),
//            FoodVertical("5","Egg Pizza",R.drawable.egg_pizza_removebg_preview,"LKR 550")
//        )))
//        productList.add(HomeHorModel("2","Burger", R.drawable.ic_baseline_lunch_dining_24, listOf(
//            FoodVertical("1","Chicken burger",R.drawable.chicken_burger_removebg_preview,"LKR 350"),
//            FoodVertical("2","Crispy Submarine",R.drawable.chicken_submarine_removebg_preview,"LKR 250"),
//            FoodVertical("3","Tea bun",R.drawable.tea_bun_removebg_preview,"LKR 300"),
//            FoodVertical("4","Fish bun",R.drawable.fish_bun_removebg_preview,"LKR 450"),
//            FoodVertical("5","Beef burger",R.drawable.chicken_burger_removebg_preview,"LKR 550")
//        )))
//        productList.add(HomeHorModel("3","Drinks", R.drawable.ic_baseline_local_drink_24,listOf(
//            FoodVertical("1","Almond",R.drawable.ic_baseline_local_drink_24,"LKR 350"),
//            FoodVertical("2","Mojito",R.drawable.ic_baseline_local_drink_24,"LKR 250"),
//            FoodVertical("3","Papaya",R.drawable.ic_baseline_local_drink_24,"LKR 250"),
//            FoodVertical("4","Mango",R.drawable.ic_baseline_local_drink_24,"LKR 550"),
//            FoodVertical("5","Apple",R.drawable.ic_baseline_local_drink_24,"LKR 300"),
//        )))
//        productList.add(HomeHorModel("4","Cakes", R.drawable.ic_baseline_cake_24,listOf(
//            FoodVertical("1","Cup Cake",R.drawable.chocolate_cake_removebg_preview,"LKR 350"),
//            FoodVertical("2","Chocolate Cake",R.drawable.chocolate_cake_removebg_preview,"LKR 1050"),
//            FoodVertical("3","Vanilla Cake",R.drawable.chocolate_cake_removebg_preview,"LKR 950"),
//            FoodVertical("4","Brownies",R.drawable.chocolate_cake_removebg_preview,"LKR 200"),
//            FoodVertical("5","Icing Cake",R.drawable.chocolate_cake_removebg_preview,"LKR 650"),
//        )))
//    }
}