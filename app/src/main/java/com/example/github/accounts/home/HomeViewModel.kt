package com.example.github.accounts.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.room.entity.AccountEntity
import com.example.github.vo.Resource

class HomeViewModel(private val accountRepository: AccountRepository) : ViewModel() {

    fun searchAccount(username: String): LiveData<Resource<List<AccountEntity>>> =
        accountRepository.searchAccount(username)

    fun loadData(context: Context): LiveData<Resource<List<AccountEntity>>> =
        accountRepository.loadAccounts(context)
}