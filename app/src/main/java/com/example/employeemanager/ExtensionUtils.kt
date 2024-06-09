package com.example.employeemanager

import android.view.View

object ExtensionUtils {

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

}