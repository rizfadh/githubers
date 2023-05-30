package com.rizfadh.githubers.utils

import android.app.AlertDialog
import android.content.Context

object myAlert {
    fun showAlert(context: Context, message: String) = AlertDialog
        .Builder(context)
        .setMessage(message)
        .setPositiveButton("Oke") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
}