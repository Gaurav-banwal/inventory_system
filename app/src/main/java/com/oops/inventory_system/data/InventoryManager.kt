package com.oops.inventory_system.data

import android.content.Context

class InventoryManager(private val context: Context) {
    private val items = mutableListOf<InventoryItem>().apply {
        // Add some sample items for testing
        add(InventoryItem("Item 1", 100.0, 150.0, 10))
        add(InventoryItem("Item 2", 200.0, 300.0, 5))
        add(InventoryItem("Item 3", 50.0, 75.0, 20))
    }

    fun getAllItems(): List<InventoryItem> = items

    fun updateItem(item: InventoryItem) {
        val index = items.indexOfFirst { it.name == item.name }
        if (index != -1) {
            items[index] = item
        }
    }
} 