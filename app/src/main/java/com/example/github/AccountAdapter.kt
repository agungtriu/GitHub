package com.example.github

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.DetailActivity.Companion.EXTRA_DETAIL
import com.example.github.databinding.ItemAccountBinding

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.HomeViewHolder>() {
    private val listAccount = ArrayList<UsersItem>()
    fun setAccount(accounts: List<UsersItem>) {
        if (accounts.isEmpty()) return
        this.listAccount.clear()
        this.listAccount.addAll(accounts)
    }
    inner class HomeViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: UsersItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(itemView.resources
                        .getIdentifier(account.avatar, "drawable",itemView.context.packageName))
                    .into(imageviewItemaccountPhoto)
                textviewItemaccountName.text = account.name
                textviewItemaccountUsername.text = account.username
                cardviewItemAccount.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL, account)
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