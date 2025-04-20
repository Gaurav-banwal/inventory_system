package com.oops.inventory_system.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oops.inventory_system.R
import com.oops.inventory_system.databinding.FragmentHomeBinding
import com.oops.inventory_system.selection

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



        return root
    }
    private fun performSearch(query: String) {
        // Implement your search logic here
    }








    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beverages = view.findViewById<LinearLayout>(R.id.beverages)
        val noodles = view.findViewById<LinearLayout>(R.id.noodles)
           val dairy = view.findViewById<LinearLayout>(R.id.dairy)
        val confectioneries = view.findViewById<LinearLayout>(R.id.confectioneries)
        val snack = view.findViewById<LinearLayout>(R.id.snacks)
        val grocery = view.findViewById<LinearLayout>(R.id.groceries)

        beverages.setOnClickListener {
            openCategory("Beverages")
        }

        noodles.setOnClickListener {
            openCategory("Noodles")
        }

        dairy.setOnClickListener {
            openCategory("Dairy")
        }
        confectioneries.setOnClickListener {
            openCategory("confectioneries")
        }
        snack.setOnClickListener {
            openCategory("snacks")
        }
        grocery.setOnClickListener {
            openCategory("Groceries")
        }
    }

    private fun openCategory(category: String) {
        val intent = Intent(requireContext(), selection::class.java)
        intent.putExtra("category_name", category) // Pass category name
        startActivity(intent)
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}

