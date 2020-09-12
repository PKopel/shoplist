package com.example.shoplist.ui.main

import android.view.*
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.data.model.items.Item
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.where
import org.bson.types.ObjectId

class ItemListAdapter(data: OrderedRealmCollection<Item>) :
    RealmRecyclerViewAdapter<Item, ItemListAdapter.ItemListViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_layout, parent, false)
        return ItemListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val data = getItem(position)
        with(holder) {
            name.text = data?.name
            qty.text = data?.qty.toString()
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = data?.checked ?: false
            checkBox.setOnCheckedChangeListener { _, _ -> data?.checked = checkBox.isChecked }
            itemView.setOnClickListener {
                run {
                    val popup = PopupMenu(holder.itemView.context, holder.view)
                    val menu = popup.menu

                    val deleteCode = -1
                    menu.add(0, deleteCode, Menu.NONE, "Delete Task")

                    popup.setOnMenuItemClickListener { item: MenuItem? ->
                        when (item!!.itemId) {
                            deleteCode -> {
                                removeAt(data?._id!!)
                            }
                        }
                        true
                    }
                    popup.show()
                }
            }
        }
    }

    private fun removeAt(id: ObjectId) {
        val bgRealm = Realm.getDefaultInstance()
        bgRealm!!.executeTransaction {
            val item = it.where<Item>().equalTo("_id", id).findFirst()
            item?.deleteFromRealm()
        }
        bgRealm.close()
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: CardView = itemView.findViewById(R.id.item_wrapper)
        val name: TextView = view.findViewById(R.id.item_name)
        val qty: TextView = view.findViewById(R.id.item_qty)
        val checkBox: CheckBox = view.findViewById(R.id.item_check)
    }
}