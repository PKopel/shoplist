package com.example.shoplist.data.model.items

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Item(
    @Required var name: String = "Item",
    shop: Shop = Shop.Small,
    var qty: Int = 0,
    var checked: Boolean = false,
    var removed: Boolean = false
) : RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    @Required
    var _partition: String = "todo"

    @Required
    private var _shop = shop.name
}