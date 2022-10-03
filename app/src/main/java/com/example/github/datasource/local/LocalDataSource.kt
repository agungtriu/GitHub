package com.example.github.datasource.local

import androidx.lifecycle.LiveData
import com.example.github.datasource.local.room.AccountDao
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.datasource.local.room.entity.AccountEntity

class LocalDataSource private constructor(private val accountDao: AccountDao) {
    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(accountDao: AccountDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(accountDao)
    }

    fun getAllFavoriteAccounts(): LiveData<List<AccountEntity>> =
        accountDao.getAllFavoriteAccounts()

    fun getFavoriteAccountByUsername(username: String): LiveData<AccountEntity> =
        accountDao.getFavoriteAccountByUsername(username)

    fun insertFavoriteAccount(accountEntity: AccountDetailEntity) =
        accountDao.insertFavoriteAccount(accountEntity)

    fun deleteFavoriteAccountByUsername(username: String) =
        accountDao.deleteFavoriteAccountByUsername(username)
}