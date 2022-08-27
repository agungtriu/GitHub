package com.example.github

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import com.example.github.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        val extras = intent.getParcelableExtra<UsersItem>(EXTRA_DETAIL)
        if (extras != null) {
            supportActionBar?.title = extras.username
            with(detailBinding) {
                Glide.with(this@DetailActivity)
                    .load(
                        resources
                            .getIdentifier(
                                extras.avatar,
                                "drawable",
                                this@DetailActivity.packageName
                            )
                    )
                    .into(circleimageviewDetailPhoto)
                textviewDetailName.text = extras.name
                username = extras.username.toString()
                textviewDetailUsername.text = username
                textviewDetailCompany.text = extras.company
                textviewDetailLocation.text = extras.location
                textviewDetailRepository.text = extras.repository.toString()
                textviewDetailFollower.text = extras.follower.toString()
                textviewDetailFollowing.text = extras.following.toString()
            }
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
}