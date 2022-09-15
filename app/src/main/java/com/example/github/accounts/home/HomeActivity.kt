package com.example.github.accounts.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.accounts.AccountAdapter
import com.example.github.accounts.favorite.FavoriteActivity
import com.example.github.accounts.setting.SettingActivity
import com.example.github.accounts.viewmodel.ViewModelFactory
import com.example.github.databinding.ActivityHomeBinding
import com.example.github.utils.Utils.Companion.showDataNotFound
import com.example.github.utils.Utils.Companion.showLoading
import com.example.github.vo.Status

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var accountAdapter: AccountAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        homeViewModel = obtainViewModel(this@HomeActivity)

        accountAdapter = AccountAdapter()
        with(homeBinding.recyclerviewHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = accountAdapter
        }

        loadData(this)
    }

    private fun loadData(context: Context) {
        with(homeBinding) {
            showLoading(true, progressbarHome, null, null)
            homeViewModel.loadData(context).observe(this@HomeActivity) {
                when (it.status) {
                    Status.LOADING -> showLoading(false, progressbarHome, null, null)
                    Status.SUCCESS -> {
                        showLoading(false, progressbarHome, null, null)
                        if (it.data != null) {
                            accountAdapter.setAccount(it.data, false)
                        } else {
                            showDataNotFound(
                                true,
                                textviewHomeNotfound,
                                viewHome,
                                null,
                                recyclerviewHome
                            )
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false, progressbarHome, null, null)
                        if (!it.message.isNullOrEmpty()) {
                            Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                        showDataNotFound(
                            true,
                            textviewHomeNotfound,
                            viewHome,
                            null,
                            recyclerviewHome
                        )
                    }
                }
            }
        }
    }

    private fun searchAccount(username: String) {
        with(homeBinding) {
            showLoading(true, progressbarHome, null, null)
            homeViewModel.searchAccount(username).observe(this@HomeActivity) {
                when (it.status) {
                    Status.LOADING -> showLoading(false, progressbarHome, null, null)
                    Status.SUCCESS -> {
                        showLoading(false, progressbarHome, null, null)
                        if (it.data != null) {
                            accountAdapter.setAccount(it.data, true)
                            showDataNotFound(
                                false,
                                textviewHomeNotfound,
                                viewHome,
                                null,
                                recyclerviewHome
                            )
                        } else {
                            showDataNotFound(
                                true,
                                textviewHomeNotfound,
                                viewHome,
                                null,
                                recyclerviewHome
                            )
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false, progressbarHome, null, null)
                        if (!it.message.isNullOrEmpty()) {
                            Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                        showDataNotFound(
                            true,
                            textviewHomeNotfound,
                            viewHome,
                            null,
                            recyclerviewHome
                        )
                    }
                }
            }
        }
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
                    searchAccount(query)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    }
}