package com.example.github.accounts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.accounts.Injection
import com.example.github.accounts.detail.DetailViewModel
import com.example.github.accounts.detail.detailtab.FollowersViewModel
import com.example.github.accounts.detail.detailtab.FollowingViewModel
import com.example.github.accounts.favorite.FavoriteViewModel
import com.example.github.accounts.home.HomeViewModel
import com.example.github.datasource.AccountRepository

class ViewModelFactory private constructor(
    private val accountRepository: AccountRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            return FollowingViewModel(accountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}