package com.example.github.accounts.detail

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.datasource.AccountRepository
import com.example.github.datasource.local.room.entity.Account
import com.example.github.datasource.remote.ApiConfig
import com.example.github.datasource.remote.response.DetailResponse
import com.example.github.utils.Utils.Companion.simplifyNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailAccount = MutableLiveData<DetailResponse>()
    val detailAccount: LiveData<DetailResponse> = _detailAccount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetailAccount(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _detailAccount.value = DetailResponse(
                            response.body()?.gistsUrl,
                            response.body()?.reposUrl,
                            response.body()?.followingUrl,
                            response.body()?.twitterUsername,
                            response.body()?.bio,
                            response.body()?.createdAt,
                            response.body()?.login,
                            response.body()?.type,
                            response.body()?.blog,
                            response.body()?.subscriptionsUrl,
                            response.body()?.updatedAt,
                            response.body()?.siteAdmin,
                            response.body()?.company,
                            response.body()?.id,
                            simplifyNumber(response.body()?.publicRepos!!.toInt()),
                            response.body()?.gravatarId,
                            response.body()?.email,
                            response.body()?.organizationsUrl,
                            response.body()?.hireable,
                            response.body()?.starredUrl,
                            response.body()?.followersUrl,
                            response.body()?.publicGists,
                            response.body()?.url,
                            response.body()?.receivedEventsUrl,
                            simplifyNumber(response.body()?.followers!!.toInt()),
                            response.body()?.avatarUrl,
                            response.body()?.eventsUrl,
                            response.body()?.htmlUrl,
                            simplifyNumber(response.body()?.following!!.toInt()),
                            response.body()?.name,
                            response.body()?.location,
                            response.body()?.nodeId
                        )
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    private val accountRepository = AccountRepository(application)

    fun getFavoriteAccountByUsername(username: String, lifecycleOwner: LifecycleOwner) {
        accountRepository.getFavoriteAccountByUsername(username).observe(lifecycleOwner) {
            _isFavorite.value = it != null
        }
    }

    fun insert(account: Account) = accountRepository.insertFavoriteAccount(account)

    fun delete(username: String) = accountRepository.deleteFavoriteAccountByUsername(username)
}