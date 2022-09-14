package com.example.github.accounts.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.local.UserResponse
import com.example.github.datasource.local.room.entity.Account
import com.example.github.datasource.remote.ApiConfig
import com.example.github.datasource.remote.response.SearchResponse
import com.example.github.utils.Utils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listAccounts = MutableLiveData<List<Account>>()
    val listAccounts: LiveData<List<Account>> = _listAccounts

    private val _listSearchAccounts = MutableLiveData<List<Account>>()
    val listSearchAccounts: LiveData<List<Account>> = _listSearchAccounts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userItems: MutableList<Account> = mutableListOf()

    fun searchAccount(username: String) {
        userItems.clear()
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchAccount(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    if (!items.isNullOrEmpty()) {
                        for (i in items.indices) {
                            userItems.add(
                                Account(items[i]!!.login, items[i]?.avatarUrl)
                            )
                        }
                    }
                    _listSearchAccounts.value = userItems
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
    fun loadData(context: Context){
        userItems.clear()
        val jsonString = Utils.loadJSONFromAsset(context, "githubuser.json")
        val data = Gson().fromJson(jsonString, UserResponse::class.java)
        if (!data.users.isNullOrEmpty()){
            for(i in data.users.indices){
                userItems.add(
                    Account(data.users[i]!!.username!!, data.users[i]?.avatar)
                )
            }
            _listAccounts.value = userItems
        }
    }
}