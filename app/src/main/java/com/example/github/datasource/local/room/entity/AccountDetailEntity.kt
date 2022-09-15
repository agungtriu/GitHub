package com.example.github.datasource.local.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "account")
data class AccountDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "avatar")
    val avatarUrl: String? = null,
    @ColumnInfo(name = "company")
    val company: String? = null,
    @ColumnInfo(name = "location")
    val location: String? = null,
    @ColumnInfo(name = "repo")
    var publicRepos: String? = null,
    @ColumnInfo(name = "followers")
    var followers: String? = null,
    @ColumnInfo(name = "following")
    var following: String? = null
) : Parcelable