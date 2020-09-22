package com.example.shoplist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.shoplist.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))
        val query: String? =
            if (Intent.ACTION_SEARCH == intent.action)
                intent.getStringExtra(SearchManager.QUERY)
            else null
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                query = query, context = this, fm = supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_action_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).run {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setQuery(intent.getStringExtra(SearchManager.QUERY), false)
        }

        return true
    }
}