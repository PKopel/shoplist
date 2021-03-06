package com.example.shoplist.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.shoplist.data.model.Shop

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Suppress("DEPRECATION")
class SectionsPagerAdapter(
    private val query: String? = null,
    private val context: Context,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return ItemListFragment.newInstance(position, query)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(Shop.values()[position].label)
    }

    override fun getCount(): Int = 3
}