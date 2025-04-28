package com.oops.inventory_system.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.oops.inventory_system.AddItem
import com.oops.inventory_system.EditItem
import com.oops.inventory_system.Item
import com.oops.inventory_system.ItemAdapter
import com.oops.inventory_system.R
import com.oops.inventory_system.RemoveItem
import com.oops.inventory_system.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>()
    private val db = FirebaseFirestore.getInstance()
    private var firestoreListener: ListenerRegistration? = null

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
        // Remove Firestore listener when fragment is destroyed
        firestoreListener?.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            listView = view.findViewById(R.id.listview)
            if (listView == null) {
                Toast.makeText(context, "ListView is null!", Toast.LENGTH_LONG).show()
                return
            }
            
            adapter = ItemAdapter(itemList, requireContext())
            listView.adapter = adapter
            
            Toast.makeText(context, "ListView setup complete", Toast.LENGTH_SHORT).show()

            // Load items from Firestore instead of adding manually
            loadItemsFromFirestore()
            
            // Handle item clicks to edit
            listView.setOnItemClickListener { _, _, position, _ ->
                Toast.makeText(context, "Clicked on item: ${itemList[position].name}", Toast.LENGTH_SHORT).show()
                openEditItemDialog(itemList[position])
            }

            // Initialize and set button listener
            val addButton = view.findViewById<Button>(R.id.addButton)
            if (addButton != null) {
                addButton.setOnClickListener {
                    val dialog = AddItem()
                    dialog.show(childFragmentManager, "AddItemDialog")
                    // The list will be updated automatically via Firestore listener
                }
            }

            val removeButton = view.findViewById<Button>(R.id.remove)
            if (removeButton != null) {
                removeButton.setOnClickListener {
                    val dialog = RemoveItem()
                    dialog.show(parentFragmentManager, "RemoveItemDialog")
                    // The list will be updated automatically via Firestore listener
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error initializing gallery: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun loadItemsFromFirestore() {
        // Clear existing items
        itemList.clear()
        
        // Set up a real-time listener for inventory changes
        firestoreListener = db.collection("inventory")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(context, "Error loading items: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Clear the list to refresh with new data
                    itemList.clear()
                    
                    for (document in snapshot.documents) {
                        try {
                            val trackId = (document.get("trackId") as? Number)?.toInt() ?: 0
                            val name = document.getString("name") ?: "Unknown"
                            val quantity = (document.get("quantity") as? Number)?.toInt() ?: 0
                            val category = document.getString("category") ?: ""
                            val location = document.getString("location") ?: ""
                            val price = (document.get("price") as? Number)?.toInt() ?: 0
                            val buyprice = (document.get("buyprice") as? Number)?.toInt() ?: 0
                            // Create Item object with all fields
                            val item = Item(
                                trackId = trackId, 
                                quantity = quantity, 
                                name = name,
                                category = category,
                                location = location,
                                price = price,
                                buyprice = buyprice
                            )
                            itemList.add(item)
                        } catch (e: Exception) {
                            // Log or handle errors for individual items
                            continue
                        }
                    }
                    
                    // Update the UI
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun openEditItemDialog(item: Item) {
        try {
            Toast.makeText(context, "Opening edit dialog for: ${item.name}", Toast.LENGTH_SHORT).show()
            val editDialog = EditItem.newInstance(
                trackId = item.trackId,
                name = item.name,
                category = item.category,
                quantity = item.quantity,
                location = item.location,
                price = item.price,
                buyprice = item.buyprice
            )
            editDialog.show(childFragmentManager, "EditItemDialog")
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // Add this function to open details with buyprice
    private fun openItemDetails(item: Item) {
        val intent = Intent(requireContext(), com.oops.inventory_system.Details::class.java).apply {
            putExtra("item_name", item.name)
            putExtra("item_id", item.trackId.toString())
            putExtra("item_quantity", item.quantity.toString())
            putExtra("item_category", item.category)
            putExtra("item_location", item.location)
            putExtra("item_price", item.price.toString())
            putExtra("item_buyprice", item.buyprice.toString())
        }
        startActivity(intent)
    }

    // This method is kept for reference but no longer used
    private fun addItem(trackId: Int, quantity: Int, name: String) {
        itemList.add(Item(trackId, quantity, name))
        adapter.notifyDataSetChanged()
    }
}