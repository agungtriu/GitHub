package com.example.github.accounts.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.accounts.home.AccountAdapter
import com.example.github.databinding.FragmentFollowingBinding
import com.example.github.utils.Utils.Companion.showLoading

class FollowingFragment : Fragment() {
    private lateinit var fragmentFollowingBinding: FragmentFollowingBinding
    private lateinit var accountAdapter: AccountAdapter
    private val followingViewModel: FollowingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return fragmentFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountAdapter = AccountAdapter()
        with(fragmentFollowingBinding.recyclerviewFollowing) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        with(followingViewModel) {
            listAccounts.observe(viewLifecycleOwner) { accounts ->
                accountAdapter.setAccount(accounts, true)
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it, fragmentFollowingBinding.progressbarFollowing)
            }
        }

        followingViewModel.getFollowing(arguments?.getString(EXTRA_FOLLOWING).toString())
    }

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }
}