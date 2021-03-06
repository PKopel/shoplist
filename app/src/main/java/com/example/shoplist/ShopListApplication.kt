package com.example.shoplist

import android.app.Application
import android.util.Log
import com.example.shoplist.utils.TAG
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var shopListApp: App

const val SERVICE_NAME = "mongodb-atlas"

/**
 * ShopList: Sets up the shopListApp Realm App and enables Realm-specific logging in debug mode.
 */
class ShopListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        shopListApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .build()
        )

        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(
            TAG(),
            "Initialized the Realm App configuration for: ${shopListApp.configuration.appId}"
        )
    }
}