package com.example.foodtrain.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtrain.R
import com.example.foodtrain.adapters.HomeHorAdapter
import com.example.foodtrain.databinding.FragmentHomeBinding
import com.example.foodtrain.models.HomeHorModel

class HomeFragment : Fragment() {

    private val productList = mutableListOf<HomeHorModel>()
    private lateinit var adapter: HomeHorAdapter
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

//        val textView: TextView = binding.textHome
//        textView.text = "Home"
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeHorAdapter(requireContext(),productList)

        // Set up RecyclerView
                val recyclerView = view.findViewById<RecyclerView>(R.id.food_type_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.adapter = adapter

        // Add sample products
        addSampleProducts()
    }

    private fun addSampleProducts() {
        productList.add(HomeHorModel("Pizza", R.drawable.ic_baseline_local_pizza_24))
        productList.add(HomeHorModel("Burger", R.drawable.ic_baseline_lunch_dining_24))
        productList.add(HomeHorModel("Drinks", R.drawable.ic_baseline_local_drink_24))
        productList.add(HomeHorModel("Cakes", R.drawable.ic_baseline_cake_24))
        productList.add(HomeHorModel("Buns", R.drawable.ic_baseline_bakery_dining_24))


        adapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}