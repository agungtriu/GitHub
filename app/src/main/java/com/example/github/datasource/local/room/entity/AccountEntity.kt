package com.example.github.datasource.local.room.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountEntity(
    val username: String,
    val avatar: String? = null
) : Parcelable