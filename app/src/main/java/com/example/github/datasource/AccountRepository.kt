package com.example.github.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.github.datasource.local.LocalDataSource
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.datasource.local.room.entity.AccountEntity
import com.example.github.datasource.remote.ApiResponse
import com.example.github.datasource.remote.RemoteDataSource
import com.example.github.datasource.remote.response.*
import com.example.github.utils.AppExecutors
import com.example.github.utils.Utils.Companion.simplifyNumber
import com.example.github.vo.Resource

class AccountRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    AccountDataSource {

    override fun getAllFavoriteAccounts(): LiveData<List<AccountEntity>> =
        localDataSource.getAllFavoriteAccounts()

    override fun getFavoriteAccountByUsername(username: String): LiveData<AccountEntity> =
        localDataSource.getFavoriteAccountByUsername(username)

    override fun insertFavoriteAccount(account: AccountDetailEntity) =
        appExecutors.diskIO().execute { localDataSource.insertFavoriteAccount(account) }

    override fun deleteFavoriteAccountByUsername(username: String) =
        appExecutors.diskIO().execute { localDataSource.deleteFavoriteAccountByUsername(username) }

    override fun loadAccounts(context: Context): LiveData<Resource<List<AccountEntity>>> {
        val accountsResult = MutableLiveData<Resource<List<AccountEntity>>>()
        remoteDataSource.loadData(context, object : RemoteDataSource.LoadAccountsCallback {
            override fun onAllAccountsReceived(listAccounts: ApiResponse<List<UsersItem>>) {
                val accountList = ArrayList<AccountEntity>()
                if (!listAccounts.body.isNullOrEmpty()) {
                    for (response in listAccounts.body) {
                        if (!response.username.isNullOrEmpty()) {
                            val account = AccountEntity(
                                response.username,
                                response.avatar
                            )
                            accountList.add(account)
                        }
                    }
                    accountsResult.postValue(Resource.success(accountList))
                } else {
                    accountsResult.postValue(Resource.error(listAccounts.message, accountList))
                }
            }
        })
        return accountsResult
    }

    override fun searchAccount(
        username: String
    ): LiveData<Resource<List<AccountEntity>>> {
        val searchAccountsResult = MutableLiveData<Resource<List<AccountEntity>>>()
        remoteDataSource.searchAccount(
            username,
            object : RemoteDataSource.LoadSearchAccountsCallback {
                override fun onAllSearchAccountsReceived(listAccounts: ApiResponse<List<SearchItem>>) {
                    val accountList = ArrayList<AccountEntity>()
                    if (!listAccounts.body.isNullOrEmpty()) {
                        for (response in listAccounts.body) {
                            if (!response.login.isNullOrEmpty()) {
                                val account = AccountEntity(
                                    response.login,
                                    response.avatarUrl
                                )
                                accountList.add(account)
                            }
                        }
                        searchAccountsResult.postValue(Resource.success(accountList))
                    } else {
                        searchAccountsResult.postValue(
                            Resource.error(
                                listAccounts.message,
                                accountList
                            )
                        )
                    }
                }
            })
        return searchAccountsResult
    }

    override fun detailAccount(
        username: String
    ): LiveData<Resource<AccountDetailEntity>> {
        val detailAccountResult = MutableLiveData<Resource<AccountDetailEntity>>()
        remoteDataSource.loadDetailAccount(
            username,
            object : RemoteDataSource.LoadDetailAccountCallback {
                override fun onDetailAccountReceived(detailAccount: ApiResponse<DetailResponse>) {
                    var account: AccountDetailEntity? = null
                    if (detailAccount.body != null) {
                        if (detailAccount.body.login != null) {
                            account = AccountDetailEntity(
                                detailAccount.body.login,
                                detailAccount.body.name,
                                detailAccount.body.avatarUrl,
                                detailAccount.body.company,
                                detailAccount.body.location,
                                simplifyNumber(detailAccount.body.publicRepos!!.toFloat()),
                                simplifyNumber(detailAccount.body.followers!!.toFloat()),
                                simplifyNumber(detailAccount.body.following!!.toFloat())
                            )
                            detailAccountResult.postValue(Resource.success(account))
                        }
                    } else {
                        detailAccountResult.postValue(
                            Resource.error(
                                detailAccount.message,
                                account
                            )
                        )
                    }

                }
            })
        return detailAccountResult
    }

    override fun loadFollowers(
        username: String
    ): LiveData<Resource<List<AccountEntity>>> {
        val loadFollowersResult = MutableLiveData<Resource<List<AccountEntity>>>()
        remoteDataSource.loadFollowers(
            username,
            object : RemoteDataSource.LoadAllFollowersCallback {
                override fun onAllFollowerReceived(listFollowers: ApiResponse<List<FollowersItem>>) {
                    val accountList = ArrayList<AccountEntity>()
                    if (!listFollowers.body.isNullOrEmpty()) {
                        for (response in listFollowers.body) {
                            if (response.login.isNotEmpty()) {
                                val account = AccountEntity(
                                    response.login,
                                    response.avatarUrl
                                )
                                accountList.add(account)
                            }
                        }
                        loadFollowersResult.postValue(Resource.success(accountList))
                    } else {
                        loadFollowersResult.postValue(
                            Resource.error(
                                listFollowers.message,
                                accountList
                            )
                        )
                    }
                }
            })
        return loadFollowersResult
    }

    override fun loadFollowing(
        username: String
    ): LiveData<Resource<List<AccountEntity>>> {
        val loadFollowingResult = MutableLiveData<Resource<List<AccountEntity>>>()
        remoteDataSource.loadFollowing(
            username,
            object : RemoteDataSource.LoadAllFollowingCallback {
                override fun onAllFollowingReceived(listFollowing: ApiResponse<List<FollowingItem>>) {
                    val accountList = ArrayList<AccountEntity>()
                    if (!listFollowing.body.isNullOrEmpty()) {
                        for (response in listFollowing.body) {
                            if (response.login.isNotEmpty()) {
                                val account = AccountEntity(
                                    response.login,
                                    response.avatarUrl
                                )
                                accountList.add(account)
                            }
                        }
                        loadFollowingResult.postValue(Resource.success(accountList))
                    } else {
                        loadFollowingResult.postValue(
                            Resource.error(
                                listFollowing.message,
                                accountList
                            )
                        )
                    }
                }
            })
        return loadFollowingResult
    }

    companion object {
        @Volatile
        private var instance: AccountRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): AccountRepository =
            instance ?: synchronized(this) {
                instance ?: AccountRepository(
                    remoteData,
                    localData,
                    appExecutors
                ).apply { instance = this }
            }
    }
}