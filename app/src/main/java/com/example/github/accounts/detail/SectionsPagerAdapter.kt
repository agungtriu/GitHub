package com.example.github.accounts.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github.accounts.detail.detailtab.FollowersFragment
import com.example.github.accounts.detail.detailtab.FollowersFragment.Companion.EXTRA_FOLLOWER
import com.example.github.accounts.detail.detailtab.FollowingFragment
import com.example.github.accounts.detail.detailtab.FollowingFragment.Companion.EXTRA_FOLLOWING

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    lateinit var username: String
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                fragment.arguments = Bundle().apply {
                    putString(EXTRA_FOLLOWER, username)
                }
            }
            1 -> {
                fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putString(EXTRA_FOLLOWING, username)
                }
            }
        }
        return fragment as Fragment
    }
}