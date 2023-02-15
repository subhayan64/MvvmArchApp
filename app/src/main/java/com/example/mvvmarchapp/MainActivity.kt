package com.example.mvvmarchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchapp.databinding.ActivityMainBinding
import com.example.mvvmarchapp.view.ItemListAdapter
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.example.mvvmarchapp.viewmodel.ProductsViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvItems.layoutManager = LinearLayoutManager(this)

        val repository = (application as MyApplication).productsRepository

        productsViewModel = ViewModelProvider(
            this, ProductsViewModelFactory(
                repository
            )
        ).get(ProductsViewModel::class.java)

        productsViewModel.products.observe(this) {
            Log.d("Subhayan", "onCreate: ${it.toString()}")

            productsViewModel.products.value?.data?.items?.let {
                adapter = ItemListAdapter(it)
                binding.rvItems.adapter = adapter

            }
        }
    }
}