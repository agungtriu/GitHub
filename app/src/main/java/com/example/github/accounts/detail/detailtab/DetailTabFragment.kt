package com.example.github.accounts.detail.detailtab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.accounts.AccountAdapter
import com.example.github.databinding.FragmentDetailTabBinding
import com.example.github.utils.Utils.Companion.showLoading

class DetailTabFragment : Fragment() {
    private lateinit var fragmentDetailTabBinding: FragmentDetailTabBinding
    private lateinit var accountAdapter: AccountAdapter
    private val detailTabViewModel: DetailTabViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDetailTabBinding = FragmentDetailTabBinding.inflate(inflater, container, false)
        return fragmentDetailTabBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter()
        with(fragmentDetailTabBinding.recyclerviewFollowers) {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            adapter = accountAdapter
        }
        with(detailTabViewModel) {
            listAccounts.observe(viewLifecycleOwner) { accounts ->
                accountAdapter.setAccount(accounts, true)
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it, fragmentDetailTabBinding.progressbarFollowers)
            }
        }

        when {
            arguments?.getString(EXTRA_FOLLOWER).toString().isNotEmpty() -> {
                detailTabViewModel.getFollowers(arguments?.getString(EXTRA_FOLLOWER).toString())
            }
            arguments?.getString(EXTRA_FOLLOWING).toString().isNotEmpty() -> {
                detailTabViewModel.getFollowing(arguments?.getString(EXTRA_FOLLOWER).toString())
            }
        }
    }

    companion object {
        const val EXTRA_FOLLOWER = "extra_follower"
        const val EXTRA_FOLLOWING = "extra_following"
    }
}