package com.example.heal_go.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.heal_go.R

class LoadingDialog(myActivity: Activity? = null, myFragment: FragmentActivity? = null) {
    private val activity: Activity? = myActivity
    private val fragment: FragmentActivity? = myFragment
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder: AlertDialog.Builder? = (activity ?: fragment)?.let { AlertDialog.Builder(it, R.style.WrapContentDialog) }
        val inflater: LayoutInflater? = activity?.layoutInflater ?: fragment?.layoutInflater
        builder?.setView(inflater?.inflate(R.layout.loading_dialog, null))
        builder?.setCancelable(false)
        dialog = builder?.create()!!
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun dismissDialog() {
        if (this::dialog.isInitialized) {
            dialog.dismiss()
        }
    }
}