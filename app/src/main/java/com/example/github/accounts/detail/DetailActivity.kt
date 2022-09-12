package com.example.github.accounts.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.app.ShareCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }

    private lateinit var detailBinding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        with(detailViewModel) {
            detailAccount.observe(this@DetailActivity) {
                with(detailBinding) {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(circleimageviewDetailPhoto)
                    imageviewDetailCompany.visibility =
                        if (it.company.isNullOrEmpty()) View.GONE else View.VISIBLE
                    textviewDetailCompany.text = it.company
                    imageviewDetailLocation.visibility =
                        if (it.location.isNullOrEmpty()) View.GONE else View.VISIBLE
                    textviewDetailLocation.text = it.location
                    textviewDetailName.text = it.name
                    textviewDetailFollower.text = it.followers
                    textviewDetailFollowing.text = it.following
                    textviewDetailRepository.text = it.publicRepos
                }
            }

            isLoading.observe(this@DetailActivity) {
                showLoading(it)
            }
        }

        if (!intent.getStringExtra(EXTRA_DETAIL).isNullOrEmpty()) {
            username = intent.getStringExtra(EXTRA_DETAIL).toString()
            supportActionBar?.title = username
            detailViewModel.getDetailAccount(username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.viewpager_detail)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tablayout_detail)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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
            } else {
                progressbarDetail.visibility = View.GONE
                viewDetail.visibility = View.GONE
            }
        }
    }
}