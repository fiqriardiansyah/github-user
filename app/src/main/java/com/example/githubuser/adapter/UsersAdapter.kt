package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.UserItemBinding
import com.example.githubuser.model.User

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var users = ArrayList<User>()
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    fun onClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    fun setData(users: ArrayList<User>){
        this.users = users
    }

    inner class UsersViewHolder(val binding:UserItemBinding ): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        with(holder){
            with(users[position]){
                binding.nameDetail.text = username
                Glide.with(itemView).load(avatar).into(binding.profileImageDetail)
                itemView.setOnClickListener {
                    onItemClickListener.onItemClick(User(0,username,avatar,"","","",0,0,"","",0))
                }
            }
        }
    }

    override fun getItemCount(): Int = users.size
}