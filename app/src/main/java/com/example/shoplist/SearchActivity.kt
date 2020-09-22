package com.example.shoplist

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.shoplist.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
    }
}