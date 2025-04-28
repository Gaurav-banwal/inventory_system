package com.oops.inventory_system.data

data class InventoryItem(
    var name: String,
    var costPrice: Double,
    var sellingPrice: Double,
    var quantity: Int,
    var soldQuantity: Int = 0
) 