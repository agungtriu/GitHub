package com.example.github.accounts

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.accounts.detail.DetailActivity
import com.example.github.accounts.detail.DetailActivity.Companion.EXTRA_DETAIL
import com.example.github.databinding.ItemAccountBinding
import com.example.github.datasource.local.room.entity.Account
import com.example.github.helper.AccountDiffCallback

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.HomeViewHolder>() {
    private val listAccount = ArrayList<Account>()
    private var _isApi = false
    fun setAccount(accounts: List<Account>, isApi: Boolean) {
        val diffCallback = AccountDiffCallback(listAccount, accounts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        if (listAccount.isNotEmpty())
            listAccount.clear()
        listAccount.addAll(accounts)
        _isApi = isApi
        diffResult.dispatchUpdatesTo(this)
    }

    inner class HomeViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            with(binding) {
                if (_isApi) {
                    Glide.with(itemView.context)
                        .load(account.avatar)
                        .into(imageviewItemaccountPhoto)
                } else {
                    Glide.with(itemView.context)
                        .load(
                            itemView.resources
                                .getIdentifier(
                                    account.avatar,
                                    "drawable",
                                    itemView.context.packageName
                                )
                        )
                        .into(imageviewItemaccountPhoto)

                }
                textviewItemaccountUsername.text = account.username
                cardviewItemAccount.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL, account.username)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val accounts = listAccount[position]
        holder.bind(accounts)
    }

    override fun getItemCount(): Int = listAccount.size
}