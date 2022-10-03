package com.example.github.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.datasource.local.room.entity.AccountEntity
import com.example.github.vo.Resource

interface AccountDataSource {
    fun getAllFavoriteAccounts(): LiveData<List<AccountEntity>>

    fun getFavoriteAccountByUsername(username: String): LiveData<AccountEntity>

    fun insertFavoriteAccount(account: AccountDetailEntity)

    fun deleteFavoriteAccountByUsername(username: String)

    fun loadAccounts(context: Context): LiveData<Resource<List<AccountEntity>>>

    fun searchAccount(username: String): LiveData<Resource<List<AccountEntity>>>

    fun detailAccount(username: String): LiveData<Resource<AccountDetailEntity>>

    fun loadFollowers(username: String): LiveData<Resource<List<AccountEntity>>>

    fun loadFollowing(username: String): LiveData<Resource<List<AccountEntity>>>
}