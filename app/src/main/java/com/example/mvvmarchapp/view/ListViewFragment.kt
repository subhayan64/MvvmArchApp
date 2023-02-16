package com.example.mvvmarchapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchapp.MyApplication
import com.example.mvvmarchapp.databinding.FragmentListViewBinding
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.example.mvvmarchapp.viewmodel.ProductsViewModelFactory

class ListViewFragment : Fragment() {

    private lateinit var productsViewModel: ProductsViewModel
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
                adapter = ItemListAdapter(it, 0)
                binding.rvLinearItems.adapter = adapter
            }
        }
        return binding.root
    }
}