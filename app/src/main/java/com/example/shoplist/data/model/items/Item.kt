package com.example.shoplist.data.model.items

import java.util.*

data class Item(
    val name: String,
    val qty: Int,
    val shop: Shop,
    val orderDate: Date,
    var checked: Boolean
)

val mock = arrayListOf(
    Item(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, etc.",
        12345,
        Shop.Small,
        Date(0),
        false
    ),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false),
    Item("chleb", 1, Shop.Small, Date(0), false),
    Item("masło", 1, Shop.Market, Date(1), false),
    Item("dżem", 1, Shop.Small, Date(2), true),
    Item("ser", 2, Shop.Special, Date(3), false)
)