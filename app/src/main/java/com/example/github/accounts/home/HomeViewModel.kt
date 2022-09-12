package com.example.github.accounts.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.UsersItem
import com.example.github.datasource.remote.ApiConfig
import com.example.github.datasource.remote.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listAccounts = MutableLiveData<List<UsersItem>>()
    val listAccounts: LiveData<List<UsersItem>> = _listAccounts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userItems: MutableList<UsersItem> = mutableListOf()
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
                                UsersItem(items[i]?.login, items[i]?.avatarUrl)
                            )
                        }
                    }
                    _listAccounts.value = userItems
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}