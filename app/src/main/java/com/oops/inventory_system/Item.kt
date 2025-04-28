package com.oops.inventory_system

data class Item(
    val trackId: Int,
    var quantity: Int,
    val name: String,
    val category: String = "",
    val location: String = "",
    val price: Int = 0,
    val buyprice :Int=0
)
