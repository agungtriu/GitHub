package com.example.github.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
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

        fun simplifyNumber(number: Int): String {
            return if (number.div(1000000) >= 1) {
                number.div(1000000).toString().plus("M")
            } else if (number.div(1000) >= 1) {
                number.div(1000).toString().plus("K")
            } else {
                number.toString()
            }
        }

        fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
            progressBar.visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}