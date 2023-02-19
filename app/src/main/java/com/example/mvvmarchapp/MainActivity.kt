package com.example.mvvmarchapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.mvvmarchapp.databinding.ActivityMainBinding
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val productsViewModel: ProductsViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fListViewFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(binding.bnvBottomNav.id)

        setupWithNavController(bottomNavigationView, navController)

        binding.iAppBarLayout.svSearchView.clearFocus()
        binding.iAppBarLayout.svSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchForItem(newText)
                return true
            }

        })
    }

    private fun searchForItem(newText: String?) {
            Log.d("subhayan", newText.toString())
            productsViewModel.onSearchTextChanged(newText)
        }
    }



