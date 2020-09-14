package com.example.shoplist.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey var _id: String = "",
    var _partition: String = "",
    var name: String = ""
) : RealmObject()