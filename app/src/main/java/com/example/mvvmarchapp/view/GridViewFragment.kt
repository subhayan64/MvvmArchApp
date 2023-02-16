package com.example.mvvmarchapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchapp.MyApplication
import com.example.mvvmarchapp.R
import com.example.mvvmarchapp.databinding.FragmentGridViewBinding
import com.example.mvvmarchapp.databinding.FragmentListViewBinding
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.example.mvvmarchapp.viewmodel.ProductsViewModelFactory


class GridViewFragment : Fragment() {
    private lateinit var productsViewModel: ProductsViewModel
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

        //initialising ViewModel
        val repository = (activity?.applicationContext as MyApplication).productsRepository
        productsViewModel = ViewModelProvider(
            this, ProductsViewModelFactory(
                repository
            )
        ).get(ProductsViewModel::class.java)

        //collecting data from livedata
        productsViewModel.products.observe(requireActivity()) {
            productsViewModel.products.value?.data?.items?.let {
                adapter = ItemListAdapter(it, 1)
                binding.rvGridItems.adapter = adapter
            }
        }
        return binding.root
    }
}