package com.example.shoplist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import io.realm.mongodb.sync.SyncConfiguration

class ShopListActivity : AppCompatActivity() {
    private var realm: Realm? = null
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
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

        val user = shopListApp.currentUser()

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            val partition = preferences.getString(
                getString(R.string.preference_partition),
                null
            ) ?: user.id

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
        }

        checkFabRmVisibility()
    }

    override fun onStop() {
        super.onStop()
        realm?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_bar, menu)
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
            val user = shopListApp.currentUser()
            val partition = preferences.getString(
                getString(R.string.preference_partition),
                user?.id
            )

            if (partition != null) {
                FirebaseMessaging.getInstance().subscribeToTopic(partition)
            }

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
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
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
            if (preferences.getBoolean(getString(R.string.preference_left_fab), true))
                View.VISIBLE
            else
                View.GONE
    }
}