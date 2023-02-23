package com.example.mvvmarchapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmarchapp.adapter.ItemListAdapter
import com.example.mvvmarchapp.databinding.FragmentGridViewBinding
import com.example.mvvmarchapp.others.utilfunctions.OnSwipeTouchListener
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GridViewFragment : Fragment() {
    private val productsViewModel: ProductsViewModel by activityViewModels()
    private lateinit var binding: FragmentGridViewBinding
    private lateinit var adapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGridViewBinding.inflate(inflater, container, false)
        //assign layout manager
        binding.rvGridItems.layoutManager = GridLayoutManager(activity, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsViewModel.items.observe(viewLifecycleOwner) { items ->
            items?.let {
                adapter = ItemListAdapter(it, 1)
                binding.rvGridItems.adapter = adapter
            }
        }

        binding.rvGridItems.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                println("onSwipeRight")
                productsViewModel.onSwipeRight()
            }
        })
        binding.srlSwipeRefreshGrid.setOnRefreshListener {
            productsViewModel.updateFromApi()
            if (binding.srlSwipeRefreshGrid.isRefreshing) {
                binding.srlSwipeRefreshGrid.isRefreshing = false
            }
        }
    }
}