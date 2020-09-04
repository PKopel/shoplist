package com.example.shoplist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.model.Item
import com.example.shoplist.model.Shop
import java.util.*

class ItemListAdapter(private val shop: Shop) :
    RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    private val mock = arrayOf(
        Item("chleb", 1, Shop.Small, Date(0), false),
        Item("masło", 1, Shop.Market, Date(1), false),
        Item("dżem", 1, Shop.Small, Date(2), false),
        Item("ser", 2, Shop.Special, Date(3), false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_layout, parent, false)
        return ItemListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val item = mock[position]
        with(holder) {
            name.text = item.name
            qty.text = item.qty.toString()
            checkBox.isChecked = item.checked
        }
    }

    override fun getItemCount(): Int = mock.size


    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val view: CardView = itemView.findViewById(R.id.item_wrapper)
        val name: TextView = view.findViewById(R.id.item_name)
        val qty: TextView = view.findViewById(R.id.item_qty)
        val checkBox: CheckBox = view.findViewById(R.id.item_check)
    }
}