package com.oops.inventory_system.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oops.inventory_system.Item
import com.oops.inventory_system.ItemAdapter
import com.oops.inventory_system.R
import com.oops.inventory_system.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>() // Correct type

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listview) // Corrected ListView ID
        adapter = ItemAdapter(itemList, requireContext()) // Use 'requireContext()' for Fragment context
        listView.adapter = adapter

        // Example: Adding an item dynamically
        addItem(1, 2, "Example Item1")
        addItem(3, 2, "Example Item2")
        addItem(4, 2, "Example Item3")
        addItem(5, 2, "Example Item4")
    }

    private fun addItem(trackId: Int, quantity: Int, name: String) {
        itemList.add(Item(trackId, quantity, name)) // Using correct 'Item' class
        adapter.notifyDataSetChanged() // Refresh ListView
    }
}