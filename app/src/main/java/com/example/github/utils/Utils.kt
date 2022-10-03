package com.example.github.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class Utils {
    companion object {
        fun loadJSONFromAsset(context: Context, fileName: String): String? {
            return try {
                context.assets.open(fileName)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun simplifyNumber(number: Float): String {
            return if (number.div(1000000) >= 1) {
                String.format("%.1fM", number / 1000000)
            } else if (number.div(1000) >= 1) {
                String.format("%.1fK", number / 1000)
            } else {
                String.format("%.0f", number)
            }
        }

        fun showLoading(
            isLoading: Boolean, progressBar: ProgressBar, view: View?, fab: FloatingActionButton?
        ) {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                if (view != null) {
                    view.visibility = View.VISIBLE
                }
                if (fab != null) {
                    fab.visibility = View.GONE
                }
            } else {
                progressBar.visibility = View.GONE
                if (view != null) {
                    view.visibility = View.GONE
                }
                if (fab != null) {
                    fab.visibility = View.VISIBLE
                }
            }
        }

        fun showDataNotFound(
            isNotFound: Boolean,
            textView: TextView,
            view: View?,
            fab: FloatingActionButton?,
            rv: RecyclerView?
        ) {
            if (isNotFound) {
                textView.visibility = View.VISIBLE
                if (view != null) {
                    view.visibility = View.VISIBLE
                }
                if (fab != null) {
                    fab.visibility = View.GONE
                }
                if (rv != null) {
                    rv.visibility = View.GONE
                }
            } else {
                textView.visibility = View.GONE
                if (view != null) {
                    view.visibility = View.GONE
                }
                if (fab != null) {
                    fab.visibility = View.VISIBLE
                }
                if (rv != null) {
                    rv.visibility = View.VISIBLE
                }
            }
        }
    }
}