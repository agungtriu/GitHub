package com.example.github.accounts.detail.detailtab

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.room.entity.AccountEntity
import com.example.github.vo.Resource

class FollowersViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    fun getFollowers(username: String): LiveData<Resource<List<AccountEntity>>> =
        accountRepository.loadFollowers(username)
}