package com.example.shoplist.data.model.items

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Item(
    @Required var name: String,
    @Required var qty: Int,
    @Required var shop: Shop,
    var checked: Boolean = false,
    var removed: Boolean = false
) : RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    @Required
    var partition: String = "todo"
}