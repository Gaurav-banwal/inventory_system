package com.oops.inventory_system.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oops.inventory_system.AddItem
import com.oops.inventory_system.Item
import com.oops.inventory_system.ItemAdapter
import com.oops.inventory_system.R
import com.oops.inventory_system.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>()

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



        listView = view.findViewById(R.id.listview)
        adapter = ItemAdapter(itemList, requireContext())
        listView.adapter = adapter

        // Example items
        addItem(1, 2, "Example Item1")
        addItem(3, 2, "Example Item2")
        addItem(4, 2, "Example Item3")
        addItem(5, 2, "Example Item4")

        // Initialize and set button listener
        val addButton = view.findViewById<Button>(R.id.addButton) // Make sure you add this button in XML
        addButton.setOnClickListener {
            val dialog = AddItem()
            dialog.show(childFragmentManager, "AddItemDialog")
        }
    }

    private fun addItem(trackId: Int, quantity: Int, name: String) {
        itemList.add(Item(trackId, quantity, name))
        adapter.notifyDataSetChanged()
    }
}