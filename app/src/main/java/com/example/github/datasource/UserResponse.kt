package com.example.github.datasource

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val users: List<UsersItem?>? = null
) : Parcelable
