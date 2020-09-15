package com.example.shoplist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.shoplist.data.model.Item
import com.example.shoplist.ui.login.LoginActivity
import com.example.shoplist.ui.main.AddDialogFragment
import com.example.shoplist.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class ShopListActivity : AppCompatActivity() {
    private var realm: Realm? = null
    private var user: User? = null

    override fun onStart() {
        super.onStart()
        try {
            user = shopListApp.currentUser()
        } catch (e: IllegalStateException) {
            Log.w(TAG(), e)
        }
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {

            val preferences =
                getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
            val partition = preferences.getString(
                getString(R.string.preference_partition_key),
                user?.id
            )

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
    }

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
            AddDialogFragment(realm!!, viewPager.currentItem).show(
                supportFragmentManager,
                "add_item"
            )
        }

        fabRm.setOnClickListener {
            realm?.executeTransaction { realm ->
                val itemsToRemove = realm.where<Item>().equalTo("checked", true).findAll()
                itemsToRemove.deleteAllFromRealm()//.setBoolean("removed", true)
            }
            val recycler = findViewById<RecyclerView>(R.id.item_list)
            recycler.adapter?.notifyDataSetChanged()
            Snackbar.make(recycler, resources.getText(R.string.remove_info), Snackbar.LENGTH_LONG)
                .setAction("Remove checked", null).show()
        }
    }

    override fun onStop() {
        super.onStop()
        user.run {
            realm?.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_remove -> {
            realm?.executeTransaction { realm ->
                val itemsToRemove = realm.where<Item>().equalTo("checked", true).findAll()
                itemsToRemove.deleteAllFromRealm()//.setBoolean("removed", true)
            }
            val recycler = findViewById<RecyclerView>(R.id.item_list)
            recycler.adapter?.notifyDataSetChanged()
            Snackbar.make(recycler, resources.getText(R.string.remove_info), Snackbar.LENGTH_LONG)
                .setAction("Remove checked", null).show()
            true
        }

        R.id.action_log_out -> {
            shopListApp.currentUser()?.logOutAsync {
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
}