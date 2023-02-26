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

/**
 * Main and only activity of the project. Corresponding activity_main.xml is responsible for
 * rendering the App bar, NavHostFragment and BottomNavigationView.
 *
 * View binding is used to set the content view and also to set up NavController
 *
 * Dagger-hilt is used in the project for dependency injection, hence view-model's object is
 * accessed using Lazy delegate.
 *
 * Inside [onCreate]:
 * - Listener to the SearchView is added to trigger search function in view-model and update
 *   the livedata.
 * - Livedata from view-model is observed to trigger navigation between fragments, whenever
 *   swipe gesture is detected.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private val productsViewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fListViewFragment) as NavHostFragment
        navController = navHostFragment.navController

        mBinding.bnvBottomNav.setupWithNavController(navController)

        //for searchView: clear focus on search view on app view create
        mBinding.iAppBarLayout.svSearchView.clearFocus()
        mBinding.iAppBarLayout.svSearchView.setOnQueryTextListener(object :
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
                mBinding.bnvBottomNav.menu.findItem(R.id.gridViewFragment),
                navController
            )
        }

        productsViewModel.swipeRightTrigger.observe(this) {
            NavigationUI.onNavDestinationSelected(
                mBinding.bnvBottomNav.menu.findItem(R.id.listViewFragment),
                navController
            )
        }
    }
}



