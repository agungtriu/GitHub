package com.example.github.accounts.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.accounts.viewmodel.ViewModelFactory
import com.example.github.databinding.ActivityDetailBinding
import com.example.github.datasource.local.room.entity.AccountDetailEntity
import com.example.github.utils.Utils.Companion.showDataNotFound
import com.example.github.utils.Utils.Companion.showLoading
import com.example.github.vo.Status
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var username: String
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        detailViewModel = obtainViewModel(this@DetailActivity)

        if (!intent.getStringExtra(EXTRA_DETAIL).isNullOrEmpty()) {
            username = intent.getStringExtra(EXTRA_DETAIL).toString()
            loadDetail(username)
            loadStateFavorite(username)
            supportActionBar?.title = username
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun loadDetail(username: String) {
        with(detailBinding) {
            showLoading(true, progressbarDetail, viewDetail, fabFavorite)
            detailViewModel.getDetailAccount(username).observe(this@DetailActivity) {
                if (it != null) {
                    when (it.status) {
                        Status.LOADING -> showLoading(
                            false,
                            progressbarDetail,
                            viewDetail,
                            fabFavorite
                        )
                        Status.SUCCESS -> {
                            showLoading(false, progressbarDetail, viewDetail, fabFavorite)
                            if (it.data != null) {
                                val account = it.data
                                with(detailBinding) {
                                    Glide.with(this@DetailActivity)
                                        .load(account.avatarUrl)
                                        .into(circleimageviewDetailPhoto)
                                    imageviewDetailCompany.visibility =
                                        if (account.company.isNullOrEmpty()) View.GONE else View.VISIBLE
                                    textviewDetailCompany.text = account.company
                                    imageviewDetailLocation.visibility =
                                        if (account.location.isNullOrEmpty()) View.GONE else View.VISIBLE
                                    textviewDetailLocation.text = account.location
                                    textviewDetailName.text = account.name
                                    textviewDetailFollower.text = account.followers
                                    textviewDetailFollowing.text = account.following
                                    textviewDetailRepository.text = account.publicRepos
                                }
                                favoriteListener(account)
                                viewPagerDetail(account.username)
                            } else {
                                showDataNotFound(
                                    true,
                                    textviewDetailNotfound,
                                    viewDetail,
                                    fabFavorite,
                                    null
                                )
                            }
                        }
                        Status.ERROR -> {
                            showLoading(false, progressbarDetail, viewDetail, fabFavorite)
                            if (!it.message.isNullOrEmpty()) {
                                Toast.makeText(this@DetailActivity, it.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                            showDataNotFound(
                                true,
                                textviewDetailNotfound,
                                viewDetail,
                                fabFavorite,
                                null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadStateFavorite(username: String) {
        detailViewModel.getFavoriteAccountByUsername(username).observe(this@DetailActivity) {
            if (it != null) {
                isFavorite = true
                detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_solid)
            } else {
                isFavorite = false
                detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }

    private fun viewPagerDetail(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.viewpager_detail)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tablayout_detail)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun favoriteListener(account: AccountDetailEntity) {
        detailBinding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                detailViewModel.delete(account.username)
            } else {
                detailViewModel.insert(account)
            }
            loadStateFavorite(account.username)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val mimeType = "text/plain"
                ShareCompat.IntentBuilder(this@DetailActivity)
                    .setType(mimeType)
                    .setChooserTitle(getString(R.string.all_share))
                    .setText("Check https://github.com/$username")
                    .startChooser()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }
}