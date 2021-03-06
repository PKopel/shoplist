package com.example.shoplist.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.data.model.Item
import com.example.shoplist.data.model.Shop
import com.example.shoplist.shopListApp
import io.realm.Realm

class AddDialogFragment(private val position: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_form, null)
            val preferences =
                it.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
            val partition = preferences.getString(
                getString(R.string.preference_partition),
                shopListApp.currentUser()!!.id
            )
            builder.setView(view)
                .setPositiveButton(R.string.add_button) { _, _ ->
                    val nameView = view.findViewById<TextView>(R.id.item_form_name)
                    val qtyView = view.findViewById<TextView>(R.id.item_form_qty)
                    val name = nameView.text.toString()
                    val qty = Integer.parseInt(qtyView.text.toString())
                    insert(
                        Item(
                            name = name,
                            shop = Shop.values()[position].name,
                            qty = qty,
                            _partition = partition!!
                        )
                    )
                    it.findViewById<RecyclerView>(R.id.item_list).adapter?.notifyDataSetChanged()
                }
                .setNegativeButton(R.string.cancel_button) { _, _ -> dialog?.cancel() }
            builder.create().apply { setTitle(R.string.new_item) }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun insert(item: Item) {
        Realm.getDefaultInstance()?.run {
            executeTransactionAsync {
                it.insert(item)
            }
            close()
        }
    }
}