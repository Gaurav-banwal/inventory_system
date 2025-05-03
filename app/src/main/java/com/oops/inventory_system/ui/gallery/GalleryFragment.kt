package com.oops.inventory_system.ui.gallery

//for the frontend
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

//for firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
//for add item and changes in inventory
import com.oops.inventory_system.AddItem
import com.oops.inventory_system.EditItem
import com.oops.inventory_system.Item
import com.oops.inventory_system.R
import com.oops.inventory_system.RemoveItem
//not used
import com.oops.inventory_system.SellItem

import com.oops.inventory_system.databinding.FragmentGalleryBinding

import android.widget.BaseAdapter
import android.widget.TextView

class GalleryFragment : Fragment() {
          //list view in gallery
    private lateinit var listView: ListView
    private lateinit var adapter: ItemAdapter
    //create a list of items
    private val itemList = mutableListOf<Item>()
    //firebase thing
    private val db = FirebaseFirestore.getInstance()
    //getting data from firebase
    private var firestoreListener: ListenerRegistration? = null

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            listView = view.findViewById(R.id.listview)
            adapter = ItemAdapter()
            listView.adapter = adapter
            
            // Load items from Firestore
            loadItemsFromFirestore()
            
            // SearchView logic
            val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.isNullOrBlank()) return false
                    val trimmed = query.trim()
                    // Try to find by ID
                    val byId = itemList.find { it.trackId.toString() == trimmed }

                    //if user search a item and find it , item will open edit item
                    if (byId != null) {
                        openEditItemDialog(byId)
                        return true
                    }
                    // Try to find by name (case-insensitive)
                    val byName = itemList.find { it.name.equals(trimmed, ignoreCase = true) }
                    //doing same for search by name
                    if (byName != null) {
                        openEditItemDialog(byName)
                        return true
                    }
                    // Try partial match
                    //try if item is half written , knor noodles and youu search knor
                    val partial = itemList.find { it.name.contains(trimmed, ignoreCase = true) }
                    if (partial != null) {
                        openEditItemDialog(partial)
                        return true
                    }

                    //end case no item ofound
                    Toast.makeText(context, "No item found for: $trimmed", Toast.LENGTH_SHORT).show()
                    return true
                }//
                override fun onQueryTextChange(newText: String?): Boolean = false
            })

            // Handle item clicks to edit
            listView.setOnItemClickListener { _, _, position, _ ->
                openEditItemDialog(itemList[position])
            }

            // Initialize and set button listeners
            //for add item

            view.findViewById<Button>(R.id.addButton)?.setOnClickListener {
                AddItem().show(childFragmentManager, "AddItemDialog")
            }
//for sell item
//            view.findViewById<Button>(R.id.sellButton)?.setOnClickListener {
//                SellItem().show(childFragmentManager, "SellItemDialog")
//            }

            view.findViewById<Button>(R.id.remove)?.setOnClickListener {
                RemoveItem().show(parentFragmentManager, "RemoveItemDialog")
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error initializing gallery: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun loadItemsFromFirestore() {
        itemList.clear()
        //setting  path in firebase
        //addsnapshot for real time database
        firestoreListener = db.collection("inventory")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(context, "Error loading items: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    itemList.clear()
                    //get into doc for data
                    for (document in snapshot.documents) {
                        try {
                            //catching data
                            val trackId = (document.get("trackId") as? Number)?.toInt() ?: 0
                            val name = document.getString("name") ?: "Unknown"
                            val quantity = (document.get("quantity") as? Number)?.toInt() ?: 0
                            val category = document.getString("category") ?: ""
                            val location = document.getString("location") ?: ""
                            val price = (document.get("price") as? Number)?.toInt() ?: 0
                            val buyprice = (document.get("buyprice") as? Number)?.toInt() ?: 0

                            //storing in local for changes
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
                            continue
                        }
                    }
                    
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun openEditItemDialog(item: Item) {
        try {
            //transfer item data to edit item
            val editDialog = EditItem.newInstance(
                trackId = item.trackId,
                name = item.name,
                category = item.category,
                quantity = item.quantity,
                location = item.location,
                price = item.price,
                buyprice = item.buyprice
            )
            //open edit item
            editDialog.show(childFragmentManager, "EditItemDialog")
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
//for that list view 1 tab
    private inner class ItemAdapter : BaseAdapter() {
        //getting the size
        override fun getCount(): Int = itemList.size

        override fun getItem(position: Int): Any = itemList[position]
    //getting item id for placing it
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(requireContext())
                .inflate(R.layout.item_layout, parent, false)
//details of items
            val item = itemList[position]
            
            val nameText = view.findViewById<TextView>(R.id.itemName)
            val detailsText = view.findViewById<TextView>(R.id.itemDetails)
            val quantityText = view.findViewById<TextView>(R.id.quantityText)
            val plusButton = view.findViewById<Button>(R.id.plusButton)
            val minusButton = view.findViewById<Button>(R.id.minusButton)

            nameText.text = item.name
            detailsText.text = "CP: ₹${item.buyprice} | SP: ₹${item.price} | Stock: ${item.quantity}"
            
            quantityText.text = item.quantity.toString()

            // Make the entire item clickable for editing
            view.setOnClickListener {
                openEditItemDialog(item)
            }

            plusButton.setOnClickListener {
                item.quantity++
                db.collection("inventory")
                    .document(item.trackId.toString())
                    .update("quantity", item.quantity)
                notifyDataSetChanged()
            }

            minusButton.setOnClickListener {
                if (item.quantity > 0) {
                    // Calculate profit for one item
                    val profit = item.price - item.buyprice
                    
                    // Update quantity
                    item.quantity--
                    //getting it tranfer in fire base
                    db.collection("inventory")
                        .document(item.trackId.toString())
                        .update("quantity", item.quantity)
                        .addOnSuccessListener {
                            // Update profit in Firestore
                            db.collection("profit_data")
                                .document("current_profit")
                                .get()
                                .addOnSuccessListener { document ->
                                    val currentProfit = if (document.exists()) {
                                        (document.get("currentMonthProfit") as? Number)?.toDouble() ?: 0.0
                                    } else {
                                        0.0
                                    }
                                    
                                    val newProfit = currentProfit + profit
                                    val profitData = HashMap<String, Any>()
                                    profitData["currentMonthProfit"] = newProfit
                                    profitData["lastMonthProfit"] = (document.get("lastMonthProfit") as? Number)?.toDouble() ?: 0.0
                                    
                                    db.collection("profit_data")
                                        .document("current_profit")
                                        .set(profitData)
                                }
                        }
                    notifyDataSetChanged()
                }
            }

            return view
        }
    }
//destroyer

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        firestoreListener?.remove()
    }
}