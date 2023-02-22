package com.example.mvvmarchapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchapp.adapter.ItemListAdapter
import com.example.mvvmarchapp.databinding.FragmentListViewBinding
import com.example.mvvmarchapp.model.Item
import com.example.mvvmarchapp.others.utilfunctions.OnSwipeTouchListener
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ListViewFragment : Fragment() {

    private val productsViewModel: ProductsViewModel by activityViewModels()
    private lateinit var binding: FragmentListViewBinding
    private lateinit var adapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false)
        //assign layout manager
        binding.rvLinearItems.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemsList = arrayListOf<Item>()
        adapter = ItemListAdapter(itemsList, 0)
        binding.rvLinearItems.adapter = adapter

        //collecting data from livedata
        productsViewModel.items.observe(viewLifecycleOwner) { items ->
            items?.let {
                itemsList.clear()
                itemsList.addAll(items)
                adapter.notifyDataSetChanged()
            }
        }


        binding.rvLinearItems.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                productsViewModel.onSwipeLeft()
            }
        })


        binding.srlSwipeRefreshList.setOnRefreshListener {
            productsViewModel.updateFromApi()
            if (binding.srlSwipeRefreshList.isRefreshing) {
                binding.srlSwipeRefreshList.isRefreshing = false
            }
        }
    }
}
