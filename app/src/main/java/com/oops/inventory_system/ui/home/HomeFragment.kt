package com.oops.inventory_system.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oops.inventory_system.R
import com.oops.inventory_system.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize SearchView
        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search action
                query?.let {
                    performSearch(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change (e.g., for live suggestions)
                return false
            }
        })

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }
    private fun performSearch(query: String) {
        // Implement your search logic here
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
