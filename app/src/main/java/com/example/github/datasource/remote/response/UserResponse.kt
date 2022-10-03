package com.example.github.datasource.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val users: List<UsersItem?>? = null
) : Parcelable
