package com.example.github.datasource

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersItem(
    val username: String? = null,
    val avatar: String? = null
) : Parcelable