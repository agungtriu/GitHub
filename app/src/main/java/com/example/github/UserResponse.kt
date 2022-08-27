package com.example.github

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val users: List<UsersItem?>? = null
) : Parcelable

@Parcelize
data class UsersItem(
    val follower: Int? = null,
    val following: Int? = null,
    val name: String? = null,
    val company: String? = null,
    val location: String? = null,
    val avatar: String? = null,
    val repository: Int? = null,
    val username: String? = null
) : Parcelable
