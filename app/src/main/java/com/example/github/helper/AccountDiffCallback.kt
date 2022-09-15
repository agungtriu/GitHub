package com.example.github.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.github.datasource.local.room.entity.AccountEntity

class AccountDiffCallback(
    private val oldList: List<AccountEntity>,
    private val newList: List<AccountEntity>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].username == newList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAccount = oldList[oldItemPosition]
        val newAccount = newList[newItemPosition]
        return oldAccount.username == newAccount.username && oldAccount.avatar == newAccount.avatar
    }

}