package com.example.shoplist.data.model

import com.example.shoplist.shopListApp
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey var _id: String = "",
    var _partition: String = shopListApp.currentUser()!!.id,
    var name: String = ""
) : RealmObject()