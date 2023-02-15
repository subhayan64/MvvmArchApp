package com.example.mvvmarchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchapp.databinding.ActivityMainBinding
import com.example.mvvmarchapp.view.ItemListAdapter
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.example.mvvmarchapp.viewmodel.ProductsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var productsViewModel: ProductsViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //    private lateinit var adapter: ItemListAdapter
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fListViewFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnvBottomNav)

        setupWithNavController(bottomNavigationView, navController)

//        binding = ActivityMainBinding.inflate(layoutInflater)

//        binding.rvItems.layoutManager = LinearLayoutManager(this)

//        val repository = (application as MyApplication).productsRepository
//
//        productsViewModel = ViewModelProvider(
//            this, ProductsViewModelFactory(
//                repository
//            )
//        ).get(ProductsViewModel::class.java)

//        productsViewModel.products.observe(this) {
//            Log.d("Subhayan", "onCreate: ${it.toString()}")
//
//            productsViewModel.products.value?.data?.items?.let {
//                adapter = ItemListAdapter(it)
//                binding.rvItems.adapter = adapter
//
//            }
//        }
    }
}