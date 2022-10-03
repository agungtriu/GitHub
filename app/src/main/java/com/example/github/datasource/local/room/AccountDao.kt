package com.example.github.datasource.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.datasource.local.room.entity.AccountEntity

@Dao
interface AccountDao {

    @Query("SELECT * from account ORDER BY username ASC")
    fun getAllFavoriteAccounts(): LiveData<List<AccountEntity>>

    @Query("SELECT * from account WHERE username= :username")
    fun getFavoriteAccountByUsername(username: String): LiveData<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteAccount(account: AccountDetailEntity)

    @Query("DELETE FROM account WHERE username= :username")
    fun deleteFavoriteAccountByUsername(username: String)
}