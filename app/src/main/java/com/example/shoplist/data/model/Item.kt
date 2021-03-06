package com.example.shoplist.data.model

import com.example.shoplist.shopListApp
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Item(
    @PrimaryKey var _id: ObjectId = ObjectId(),
    var name: String = "Item",
    var qty: Int = 0,
    var checked: Boolean = false,
    var removed: Boolean = false,
    var shop: String = Shop.Small.name,
    var _partition: String = shopListApp.currentUser()!!.id,
) : RealmObject()