package com.umbrella.nasaapiapp.ui.view

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.showToast(error: Throwable) {
    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
}