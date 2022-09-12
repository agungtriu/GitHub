package com.example.github.accounts.detail.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.accounts.home.AccountAdapter
import com.example.github.databinding.FragmentFollowersBinding
import com.example.github.utils.Utils.Companion.showLoading

class FollowersFragment : Fragment() {
    private lateinit var followersBinding: FragmentFollowersBinding
    private lateinit var accountAdapter: AccountAdapter
    private val followersViewModel: FollowersViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return followersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter()
        with(followersBinding.recyclerviewFollowers) {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        with(followersViewModel) {
            listAccounts.observe(viewLifecycleOwner) { accounts ->
                accountAdapter.setAccount(accounts, true)
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it, followersBinding.progressbarFollowers)
            }
        }

        followersViewModel.getFollowers(arguments?.getString(EXTRA_FOLLOWER).toString())
    }

    companion object {
        const val EXTRA_FOLLOWER = "extra_follower"
    }
}