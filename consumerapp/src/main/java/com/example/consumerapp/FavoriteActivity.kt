package com.example.consumerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.UsersAdapter
import com.example.consumerapp.databinding.ActivityFavoriteBinding
import com.example.consumerapp.db.DbContract.UserColumns.Companion.CONTENT_URI
import com.example.consumerapp.db.DbHelper
import com.example.consumerapp.model.User

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        supportActionBar?.title = "Favorite User"
    }

    override fun onResume() {
        super.onResume()

        val cursor = contentResolver.query(CONTENT_URI,null,null,null,null)

        val users = DbHelper().parseCursorToArrayList(cursor!!)
        adapter.setData(users)
        adapter.notifyDataSetChanged()
        adapter.onClick(object: UsersAdapter.OnItemClickListener{
            override fun onItemClick(user: User) {
                val mIntent = Intent(this@FavoriteActivity,DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USER,user)
                startActivity(mIntent)
            }
        })

    }


}