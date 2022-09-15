package com.example.github.accounts

import android.content.Context
import com.example.github.accounts.setting.SettingPreferences
import com.example.github.accounts.setting.dataStore
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.LocalDataSource
import com.example.github.datasource.local.room.AccountRoomDatabase
import com.example.github.datasource.remote.RemoteDataSource
import com.example.github.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): AccountRepository {
        val database = AccountRoomDatabase.getDatabase(context)

        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.AccountDao())
        val appExecutors = AppExecutors()
        return AccountRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideSettingPreference(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}