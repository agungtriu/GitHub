package com.example.github.accounts.detail.detailtab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.accounts.AccountAdapter
import com.example.github.accounts.viewmodel.ViewModelFactory
import com.example.github.databinding.FragmentFollowersBinding
import com.example.github.utils.Utils.Companion.showDataNotFound
import com.example.github.utils.Utils.Companion.showLoading
import com.example.github.vo.Status

class FollowersFragment : Fragment() {
    private lateinit var fragmentFollowersBinding: FragmentFollowersBinding
    private lateinit var accountAdapter: AccountAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return fragmentFollowersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel = obtainViewModel(requireActivity())

        accountAdapter = AccountAdapter()
        with(fragmentFollowersBinding.recyclerviewFollowers) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        if (!arguments?.getString(EXTRA_FOLLOWER).isNullOrEmpty()) {
            loadFollowers(arguments?.getString(EXTRA_FOLLOWER).toString())
        }
    }

    private fun loadFollowers(username: String) {
        with(fragmentFollowersBinding) {
            showLoading(true, progressbarFollowers, null, null)
            followersViewModel.getFollowers(username)
                .observe(viewLifecycleOwner) {
                    if (it.data != null) {
                        when (it.status) {
                            Status.LOADING -> showLoading(false, progressbarFollowers, null, null)
                            Status.SUCCESS -> {
                                showLoading(false, progressbarFollowers, null, null)
                                if (it.data.isEmpty()) {
                                    showDataNotFound(true, textviewFollowersZero, null, null, null)
                                } else {
                                    accountAdapter.setAccount(it.data, true)
                                }
                            }
                            Status.ERROR -> {
                                showLoading(false, progressbarFollowers, null, null)
                                if (!it.message.isNullOrEmpty()) {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                                showDataNotFound(true, textviewFollowersZero, null, null, null)
                            }
                        }
                    }
                }
        }
    }

    companion object {
        const val EXTRA_FOLLOWER = "extra_follower"
    }

    private fun obtainViewModel(
        activity: FragmentActivity
    ): FollowersViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FollowersViewModel::class.java]
    }
}