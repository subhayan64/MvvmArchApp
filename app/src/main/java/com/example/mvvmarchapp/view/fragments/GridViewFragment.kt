package com.example.mvvmarchapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmarchapp.R
import com.example.mvvmarchapp.adapter.ItemListAdapter
import com.example.mvvmarchapp.databinding.FragmentGridViewBinding
import com.example.mvvmarchapp.others.Status
import com.example.mvvmarchapp.others.utilfunctions.OnSwipeTouchListener
import com.example.mvvmarchapp.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsible for rendering grid recycler view to display a grid of items.
 *
 * Inside onViewCreated:
 * - Observing items livedata to populate recycler view adapter.
 * - Listener for SwipeRefreshLayout to trigger api call.
 * - Observing status livedata to update SwipeRefreshLayout loader for different states
 * - Listener for swipe gesture on the recycler view to propogate the message to the view-model
 *   and trigger navigation from the main activity
 */
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
        binding.srlSwipeRefreshGrid.setColorSchemeColors(resources.getColor(R.color.purple_500))
        binding.srlSwipeRefreshGrid.isRefreshing = true

        productsViewModel.items.observe(viewLifecycleOwner) { items ->
            items?.let {
                adapter = ItemListAdapter(it, 1)
                binding.rvGridItems.adapter = adapter
            }
        }

        binding.srlSwipeRefreshGrid.setOnRefreshListener {
            productsViewModel.updateFromApi()
        }

        productsViewModel.status.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.srlSwipeRefreshGrid.isRefreshing = true
                }
                Status.SUCCESS -> {
                    binding.srlSwipeRefreshGrid.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.srlSwipeRefreshGrid.isRefreshing = false
                    Toast.makeText(context,  it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.srlSwipeRefreshGrid.isRefreshing = false
                }
            }
        }

        binding.rvGridItems.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                println("onSwipeRight")
                productsViewModel.onSwipeRight()
            }
        })
    }
}