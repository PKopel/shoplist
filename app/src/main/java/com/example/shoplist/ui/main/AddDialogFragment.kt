package com.example.shoplist.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.shoplist.R
import com.example.shoplist.data.model.items.Item
import com.example.shoplist.data.model.items.Shop
import com.example.shoplist.data.model.items.mock
import java.util.*

class AddDialogFragment(private val position: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_form, null)
            builder.setView(view)
                .setPositiveButton(R.string.add_button) { _, _ ->
                    val nameView = view.findViewById<TextView>(R.id.item_form_name)
                    val qtyView = view.findViewById<TextView>(R.id.item_form_qty)
                    val name = nameView.text.toString()
                    val qty = Integer.parseInt(qtyView.text.toString())
                    mock.add(
                        Item(
                            name,
                            qty,
                            Shop.values()[position],
                            Calendar.getInstance().time,
                            false
                        )
                    )
                }
                .setNegativeButton(R.string.cancel_button) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}