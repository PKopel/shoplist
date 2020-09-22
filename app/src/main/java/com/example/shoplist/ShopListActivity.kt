package com.example.shoplist

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.shoplist.data.model.Item
import com.example.shoplist.ui.login.LoginActivity
import com.example.shoplist.ui.main.AddDialogFragment
import com.example.shoplist.ui.main.SectionsPagerAdapter
import com.example.shoplist.utils.TAG
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class ShopListActivity : AppCompatActivity() {
    private var realm: Realm? = null
    private var user: User? = shopListApp.currentUser()
    private lateinit var partition: String
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                context = this,
                fm = supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        val fabRm: FloatingActionButton = findViewById(R.id.fab_remove)

        fabAdd.setOnClickListener {
            AddDialogFragment(viewPager.currentItem).show(
                supportFragmentManager,
                "add_item"
            )
        }

        fabRm.setOnClickListener {
            removeCheckedItems()
        }

        preferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }


    override fun onStart() {
        super.onStart()
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            partition = preferences.getString(
                getString(R.string.preference_partition),
                user?.id
            ) ?: "anonymus"

            FirebaseMessaging.getInstance().subscribeToTopic(partition)

            val config = SyncConfiguration.Builder(user, partition)
                .waitForInitialRemoteData()
                .build()

            Realm.setDefaultConfiguration(config)

            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    this@ShopListActivity.realm = realm
                }
            })

            findViewById<RecyclerView>(R.id.item_list)?.adapter?.notifyDataSetChanged()
            checkFabRmVisibility()
        }
    }

    override fun onStop() {
        super.onStop()
        realm?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_action_bar, menu)

        val searchComponent = ComponentName(this, SearchActivity::class.java)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).run {
            setSearchableInfo(searchManager.getSearchableInfo(searchComponent))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_remove -> {
            removeCheckedItems()
            true
        }

        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        R.id.action_log_out -> {
            logOut()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(partition)

        user?.getPush(SERVICE_NAME)?.deregisterDevice()
        user?.logOutAsync {
            if (it.isSuccess) {
                realm?.close()
                Log.v(TAG(), "user logged out")
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Log.e(TAG(), "log out failed! Error: ${it.error}")
            }
        }
    }

    private fun removeCheckedItems() {
        realm?.executeTransactionAsync {
            val itemsToRemove = it.where<Item>().equalTo("checked", true).findAll()
            itemsToRemove.deleteAllFromRealm()//.setBoolean("removed", true)
        }

        val recycler = findViewById<RecyclerView>(R.id.item_list)
        recycler.adapter?.notifyDataSetChanged()
        Snackbar.make(recycler, resources.getText(R.string.remove_info), Snackbar.LENGTH_LONG)
            .setAction("Remove checked", null).show()
    }


    private fun checkFabRmVisibility() {
        findViewById<FloatingActionButton>(R.id.fab_remove).visibility =
            if (preferences.getBoolean(getString(R.string.preference_left_fab), true)) View.VISIBLE
            else View.GONE
    }
}