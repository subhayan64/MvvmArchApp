package com.example.mvvmarchapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmarchapp.R
import com.example.mvvmarchapp.databinding.ActivityMainBinding
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
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

        binding.bnvBottomNav.setupWithNavController(navController)

        //for searchView: clear focus on search view on app view create
        binding.iAppBarLayout.svSearchView.clearFocus()
        binding.iAppBarLayout.svSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //invoke to every character input
            override fun onQueryTextChange(newText: String?): Boolean {
                //send the input string to ViewModel
                productsViewModel.onSearchTextChanged(newText)
                return true
            }
        })


        productsViewModel.swipeLeftTrigger.observe(this) {
            NavigationUI.onNavDestinationSelected(
                binding.bnvBottomNav.menu.findItem(R.id.gridViewFragment),
                navController
            )
        }

        productsViewModel.swipeRightTrigger.observe(this) {
            NavigationUI.onNavDestinationSelected(
                binding.bnvBottomNav.menu.findItem(R.id.listViewFragment),
                navController
            )
        }
    }
}



