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
import com.example.github.databinding.FragmentFollowingBinding
import com.example.github.utils.Utils.Companion.showDataNotFound
import com.example.github.utils.Utils.Companion.showLoading
import com.example.github.vo.Status

class FollowingFragment : Fragment() {
    private lateinit var fragmentFollowingBinding: FragmentFollowingBinding
    private lateinit var accountAdapter: AccountAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return fragmentFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = obtainViewModel(requireActivity())

        accountAdapter = AccountAdapter()
        with(fragmentFollowingBinding.recyclerviewFollowing) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        if (!arguments?.getString(EXTRA_FOLLOWING).isNullOrEmpty()) {
            loadFollowing(arguments?.getString(EXTRA_FOLLOWING).toString())
        }
    }

    private fun loadFollowing(username: String) {
        with(fragmentFollowingBinding) {
            showLoading(true, progressbarFollowing, null, null)
            followingViewModel.getFollowing(username)
                .observe(viewLifecycleOwner) {
                    if (it.data != null) {
                        when (it.status) {
                            Status.LOADING -> showLoading(false, progressbarFollowing, null, null)
                            Status.SUCCESS -> {
                                showLoading(false, progressbarFollowing, null, null)
                                if (it.data.isEmpty()) {
                                    showDataNotFound(true, textviewFollowingZero, null, null, null)
                                } else {
                                    accountAdapter.setAccount(it.data, true)
                                }
                            }
                            Status.ERROR -> {
                                showLoading(false, progressbarFollowing, null, null)
                                if (!it.message.isNullOrEmpty()) {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                                showDataNotFound(true, textviewFollowingZero, null, null, null)
                            }
                        }
                    }
                }
        }
    }

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }

    private fun obtainViewModel(
        activity: FragmentActivity
    ): FollowingViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FollowingViewModel::class.java]
    }
}