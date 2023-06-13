package com.example.foodtrain.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.foodtrain.Constants
import com.example.foodtrain.R
import com.example.foodtrain.adapters.OrderVerAdapter
import com.example.foodtrain.databinding.FragmentOrdersBinding
import com.example.foodtrain.fireStore.FireStoreClass
import com.example.foodtrain.models.AddToCart
import com.example.foodtrain.models.FoodType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {


    private var ordersList = ArrayList<AddToCart>()
    private lateinit var orderRecyclerView : RecyclerView
    private lateinit var swipeRefreshOrderLayout: SwipeRefreshLayout

    private var quantityChanger = 1
    private var priceList = ArrayList<Double>()
    private var price : TextView? = null
    private var changableValue : TextView? = null
    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        orderRecyclerView = root.findViewById(R.id.order_details_recyclerView)
        swipeRefreshOrderLayout = root.findViewById(R.id.orderSwipeLayout)

        ordersList.clear()

        swipeRefreshOrderLayout.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                swipeRefreshOrderLayout.isRefreshing = true
                val orders = FireStoreClass().loadOrderDetails()
                ordersList.clear()
                ordersList.addAll(orders)
                orderRecyclerView.adapter?.notifyDataSetChanged()
                swipeRefreshOrderLayout.isRefreshing = false
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            swipeRefreshOrderLayout.isRefreshing = true
            val orders = FireStoreClass().loadOrderDetails()
            ordersList.clear()
            ordersList.addAll(orders)
            orderRecyclerView.adapter?.notifyDataSetChanged()
            swipeRefreshOrderLayout.isRefreshing = false
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            swipeRefreshOrderLayout.isRefreshing = true
            val orders = FireStoreClass().loadOrderDetails()
            ordersList.clear()
            ordersList.addAll(orders)
            orderRecyclerView.adapter?.notifyDataSetChanged()
            swipeRefreshOrderLayout.isRefreshing = false
        }

        orderRecyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        orderRecyclerView.isNestedScrollingEnabled = true

        val orderAdapter = OrderVerAdapter(requireContext(),ordersList)
        orderRecyclerView.adapter = orderAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getOrderUpdates(orders : ArrayList<AddToCart>){
        ordersList.clear()
        ordersList.addAll(orders)
    }
}