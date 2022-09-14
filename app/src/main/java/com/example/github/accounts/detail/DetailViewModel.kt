package com.example.github.accounts.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.datasource.local.room.entity.AccountEntity
import com.example.github.vo.Resource

class DetailViewModel(private val accountRepository: AccountRepository) : ViewModel() {

    fun getDetailAccount(username: String): LiveData<Resource<AccountDetailEntity>> =
        accountRepository.detailAccount(username)

    fun getFavoriteAccountByUsername(username: String): LiveData<AccountEntity> =
        accountRepository.getFavoriteAccountByUsername(username)

    fun insert(account: AccountDetailEntity) = accountRepository.insertFavoriteAccount(account)

    fun delete(username: String) = accountRepository.deleteFavoriteAccountByUsername(username)
}