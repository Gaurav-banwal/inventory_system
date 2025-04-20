package com.oops.inventory_system

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ItemListAdapter(private val context: Context, private var items: List<Item>) : BaseAdapter() {

    override fun getCount(): Int = items.size
    
    override fun getItem(position: Int): Any = items[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_list_row, parent, false
        )
        
        val item = items[position]
        
        // Find views
        val nameTextView = view.findViewById<TextView>(R.id.item_name)
        val priceTextView = view.findViewById<TextView>(R.id.item_price)
        val quantityTextView = view.findViewById<TextView>(R.id.item_quantity)
        
        // Set data to views
        nameTextView.text = item.name
        priceTextView.text = "₹${item.price}"
        quantityTextView.text = "Qty: ${item.quantity}"
        
        return view
    }
    
    /**
     * Updates the adapter with a new list of items and notifies dataset changed
     */
    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
} 