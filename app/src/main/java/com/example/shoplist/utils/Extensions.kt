package com.example.shoplist.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


/**
 * Global Kotlin extension that resolves to the short version
 * of the name of the current class. Used for labelling logs.
 * */
@Suppress("FunctionName", "unused")
inline fun <reified T> T.TAG(): String = T::class.java.simpleName