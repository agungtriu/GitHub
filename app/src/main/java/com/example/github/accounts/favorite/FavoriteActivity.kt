package com.example.github.accounts.favorite

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.accounts.AccountAdapter
import com.example.github.accounts.viewmodel.ViewModelFactory
import com.example.github.databinding.ActivityFavoriteBinding
import com.example.github.utils.Utils.Companion.showDataNotFound
import com.example.github.utils.Utils.Companion.showLoading

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var accountAdapter: AccountAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)

        accountAdapter = AccountAdapter()
        with(favoriteBinding.recyclerviewFavorite) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = accountAdapter
        }

        supportActionBar?.title = getString(R.string.all_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadFavoriteAccounts() {
        with(favoriteBinding) {
            showLoading(true, progressbarFavorite, null, null)
            favoriteViewModel.getAllFavoriteAccounts().observe(this@FavoriteActivity) {
                showLoading(false, progressbarFavorite, null, null)
                if (it.isEmpty()) {
                    showDataNotFound(
                        true,
                        textviewFavoriteNotfound,
                        null,
                        null,
                        recyclerviewFavorite
                    )
                } else {
                    accountAdapter.setAccount(it, true)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onResume() {
        loadFavoriteAccounts()
        super.onResume()
    }
}