package com.oops.inventory_system

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView



class ItemAdapter(private val items: MutableList<Item>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = items[position].trackId.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val item = items[position]

        val nameText = view.findViewById<TextView>(R.id.itemName)
        val quantityText = view.findViewById<TextView>(R.id.quantity)
        val addButton = view.findViewById<Button>(R.id.addButton)
        val subtractButton = view.findViewById<Button>(R.id.subtractButton)

        nameText.text = item.name
        quantityText.text = item.quantity.toString()

        addButton.setOnClickListener {
            item.quantity++
            quantityText.text = item.quantity.toString()
            notifyDataSetChanged() // Refresh ListView
        }

        subtractButton.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                quantityText.text = item.quantity.toString()
                notifyDataSetChanged()
            }
        }

        return view
    }








}