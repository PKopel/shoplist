package com.example.shoplist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.shoplist.data.model.items.Item
import com.example.shoplist.data.model.items.mock
import com.example.shoplist.ui.login.LoginActivity
import com.example.shoplist.ui.main.AddDialogFragment
import com.example.shoplist.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import io.realm.mongodb.User

class ShopListActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        var user: User? = null
        try {
            user = shopListApp.currentUser()
        } catch (e: IllegalStateException) {
            Log.w(TAG(), e)
        }
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
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
            AddDialogFragment(viewPager.currentItem).show(supportFragmentManager, "add_item")
            findViewById<RecyclerView>(R.id.item_list).adapter?.notifyDataSetChanged()
        }

        fabRm.setOnClickListener {
            mock.removeIf(Item::checked)
            findViewById<RecyclerView>(R.id.item_list).adapter?.notifyDataSetChanged()
            Snackbar.make(it, R.string.remove_info, Snackbar.LENGTH_LONG)
                .setAction("Remove checked", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_remove -> {
            mock.removeIf(Item::checked)
            val recycler = findViewById<RecyclerView>(R.id.item_list)
            recycler.adapter?.notifyDataSetChanged()
            Snackbar.make(recycler, resources.getText(R.string.remove_info), Snackbar.LENGTH_LONG)
                .setAction("Remove checked", null).show()
            true
        }

        R.id.action_log_out -> {
            shopListApp.currentUser()?.logOutAsync {
                if (it.isSuccess)
                    startActivity(Intent(this, LoginActivity::class.java))
            }
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}