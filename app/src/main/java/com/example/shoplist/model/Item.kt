package com.example.shoplist.model

import java.util.*

data class Item(
    val name: String,
    val qty: Int,
    val shop: Shop,
    val orderDate: Date,
    var checked: Boolean
)