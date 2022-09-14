package com.example.github.accounts.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.accounts.ViewModelFactory
import com.example.github.accounts.setting.SettingPreferences
import com.example.github.accounts.setting.dataStore
import com.example.github.databinding.ActivityDetailBinding
import com.example.github.datasource.local.room.entity.Account
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var username: String
    private var _isFavorite: Boolean = false
    private lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        detailViewModel = obtainViewModel(this@DetailActivity, pref)
        if (!intent.getStringExtra(EXTRA_DETAIL).isNullOrEmpty()) {
            username = intent.getStringExtra(EXTRA_DETAIL).toString()
            detailViewModel.getDetailAccount(username)
        }

        with(detailViewModel) {
            detailAccount.observe(this@DetailActivity) { account ->
                this@DetailActivity.account = Account(account.login!!, account.avatarUrl)
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
            }
            isLoading.observe(this@DetailActivity) {
                showLoading(it)
            }
            getFavoriteAccountByUsername(username, this@DetailActivity)
            isFavorite.observe(this@DetailActivity) {
                if (it) {
                    _isFavorite = true
                    detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_solid)
                } else {
                    _isFavorite = false
                    detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }


        supportActionBar?.title = username
        viewPagerDetail()
        favoriteListener()
    }

    private fun viewPagerDetail() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.viewpager_detail)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tablayout_detail)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun favoriteListener() {
        detailBinding.fabFavorite.setOnClickListener {
            if (_isFavorite) {
                detailViewModel.delete(username)
            } else {
                detailViewModel.insert(account)
            }
            detailViewModel.getFavoriteAccountByUsername(username, this@DetailActivity)
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
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(detailBinding) {
            if (isLoading) {
                progressbarDetail.visibility = View.VISIBLE
                viewDetail.visibility = View.VISIBLE
                fabFavorite.visibility = View.GONE
            } else {
                progressbarDetail.visibility = View.GONE
                viewDetail.visibility = View.GONE
                fabFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        preferences: SettingPreferences
    ): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }
}