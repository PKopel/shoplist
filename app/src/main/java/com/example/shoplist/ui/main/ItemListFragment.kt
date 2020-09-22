package com.example.shoplist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.data.model.Item
import com.example.shoplist.data.model.Shop
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A fragment containing list of items.
 */
class ItemListFragment(private val query: String? = null) : Fragment() {

    private val realm: Realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = ItemListAdapter(
                    realm.where<Item>()
                        .equalTo("shop", Shop.values()[arguments?.getInt(ARG_SHOP_TYPE) ?: 0].name)
                        .let {
                            if (query != null) it.contains("name", query) else it
                        }
                        .findAll()
                )
                adapter?.notifyDataSetChanged()
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SHOP_TYPE = "shop_type"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionShop: Int, query: String?): ItemListFragment {
            return ItemListFragment(query).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SHOP_TYPE, sectionShop)
                }
            }
        }
    }
}