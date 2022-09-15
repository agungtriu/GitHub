package com.example.github.accounts.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.room.entity.AccountEntity

class FavoriteViewModel(private val accountRepository: AccountRepository) : ViewModel() {

    fun getAllFavoriteAccounts(): LiveData<List<AccountEntity>> =
        accountRepository.getAllFavoriteAccounts()
}