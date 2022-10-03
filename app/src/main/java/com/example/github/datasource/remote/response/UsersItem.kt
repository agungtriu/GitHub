package com.example.github.datasource.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersItem(
    val username: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val company: String? = null,
    val location: String? = null,
    var repository: String? = null,
    var follower: String? = null,
    var following: String? = null
) : Parcelable