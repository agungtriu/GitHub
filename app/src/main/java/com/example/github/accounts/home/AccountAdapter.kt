package com.example.github.accounts.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.accounts.detail.DetailActivity.Companion.EXTRA_DETAIL
import com.example.github.datasource.UsersItem
import com.example.github.accounts.detail.DetailActivity
import com.example.github.databinding.ItemAccountBinding

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.HomeViewHolder>() {
    private val listAccount = ArrayList<UsersItem>()
    private var _isOnline = false
    fun setAccount(accounts: List<UsersItem>, isOnline: Boolean) {
        this.listAccount.clear()
        this.listAccount.addAll(accounts)
        _isOnline = isOnline
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: UsersItem) {
            with(binding) {
                if (_isOnline) {
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