package com.rizfadh.githubers.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizfadh.githubers.data.api.response.UserItem
import com.rizfadh.githubers.databinding.ItemUserBinding

class UserAdapter(
    private val onItemClick: (UserItem) -> Unit
) : ListAdapter<UserItem, UserAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userItem: UserItem) {
            binding.tvUsername.text = userItem.login
            Glide.with(itemView.context)
                .load(userItem.avatarUrl)
                .into(binding.civUserImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = getItem(position)

        holder.bind(user)

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserItem> =
            object : DiffUtil.ItemCallback<UserItem>() {
                override fun areItemsTheSame(oldUser: UserItem, newUser: UserItem): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserItem, newUser: UserItem): Boolean {
                    return oldUser == newUser
                }
            }
    }
}