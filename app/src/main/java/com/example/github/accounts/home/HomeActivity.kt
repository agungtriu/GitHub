package com.example.github.accounts.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.databinding.ActivityHomeBinding
import com.example.github.datasource.UserResponse
import com.example.github.datasource.UsersItem
import com.example.github.utils.Utils.Companion.loadJSONFromAsset
import com.example.github.utils.Utils.Companion.showLoading
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var accountAdapter: AccountAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        accountAdapter = AccountAdapter()
        with(homeBinding.recyclerviewHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        with(homeViewModel) {
            listAccounts.observe(this@HomeActivity) { accounts ->
                accountAdapter.setAccount(accounts, true)
            }
            isLoading.observe(this@HomeActivity) {
                showLoading(it, homeBinding.progressbarHome)
            }
        }

        val jsonString = loadJSONFromAsset(this, "githubuser.json")
        val data = Gson().fromJson(jsonString, UserResponse::class.java)
        accountAdapter.setAccount(data.users as List<UsersItem>, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.home_queryhint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    homeViewModel.searchAccount(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}