package com.example.github.datasource.remote

import android.content.Context
import com.example.github.datasource.remote.response.*
import com.example.github.utils.Utils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

    fun loadData(context: Context, callback: LoadAccountsCallback) {
        val jsonString = Utils.loadJSONFromAsset(context, "githubuser.json")
        val data = Gson().fromJson(jsonString, UserResponse::class.java)
        if (!data.users.isNullOrEmpty()) {
            callback.onAllAccountsReceived(ApiResponse.success(data.users as List<UsersItem>))
        } else {
            callback.onAllAccountsReceived(ApiResponse.error("Data not found", null))
        }
    }

    fun searchAccount(username: String, callback: LoadSearchAccountsCallback) {
        val client = ApiConfig.getApiService().searchAccount(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onAllSearchAccountsReceived(ApiResponse.success(response.body()?.items as List<SearchItem>))
                } else {
                    callback.onAllSearchAccountsReceived(
                        ApiResponse.error(
                            "API rate limit exceeded",
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                callback.onAllSearchAccountsReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun loadDetailAccount(username: String, callback: LoadDetailAccountCallback) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onDetailAccountReceived(ApiResponse.success(response.body() as DetailResponse))
                } else {
                    callback.onDetailAccountReceived(
                        ApiResponse.error(
                            "API rate limit exceeded",
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                callback.onDetailAccountReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun loadFollowers(username: String, callback: LoadAllFollowersCallback) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowersItem>> {
            override fun onResponse(
                call: Call<List<FollowersItem>>,
                response: Response<List<FollowersItem>>
            ) {
                if (response.isSuccessful) {
                    callback.onAllFollowerReceived(ApiResponse.success(response.body() as List<FollowersItem>))
                } else {
                    callback.onAllFollowerReceived(
                        ApiResponse.error(
                            "API rate limit exceeded",
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<FollowersItem>>, t: Throwable) {
                callback.onAllFollowerReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun loadFollowing(username: String, callback: LoadAllFollowingCallback) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowingItem>> {
            override fun onResponse(
                call: Call<List<FollowingItem>>,
                response: Response<List<FollowingItem>>
            ) {
                if (response.isSuccessful) {
                    callback.onAllFollowingReceived(ApiResponse.success(response.body() as List<FollowingItem>))
                } else {
                    callback.onAllFollowingReceived(
                        ApiResponse.error(
                            "API rate limit exceeded",
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<List<FollowingItem>>, t: Throwable) {
                callback.onAllFollowingReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    interface LoadAccountsCallback {
        fun onAllAccountsReceived(listAccounts: ApiResponse<List<UsersItem>>)
    }

    interface LoadSearchAccountsCallback {
        fun onAllSearchAccountsReceived(listAccounts: ApiResponse<List<SearchItem>>)
    }

    interface LoadDetailAccountCallback {
        fun onDetailAccountReceived(detailAccount: ApiResponse<DetailResponse>)
    }

    interface LoadAllFollowersCallback {
        fun onAllFollowerReceived(listFollowers: ApiResponse<List<FollowersItem>>)
    }

    interface LoadAllFollowingCallback {
        fun onAllFollowingReceived(listFollowing: ApiResponse<List<FollowingItem>>)
    }
}