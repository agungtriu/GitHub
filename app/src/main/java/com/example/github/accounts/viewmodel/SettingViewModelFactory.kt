package com.example.github.accounts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.accounts.Injection
import com.example.github.accounts.setting.SettingPreferences
import com.example.github.accounts.setting.SettingViewModel
import com.example.github.splashscreen.SplashScreenViewModel

class SettingViewModelFactory private constructor(
    private val preferences: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: SettingViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): SettingViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SettingViewModelFactory::class.java) {
                    INSTANCE = SettingViewModelFactory(Injection.provideSettingPreference(context))
                }
            }
            return INSTANCE as SettingViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(preferences) as T
        } else if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}