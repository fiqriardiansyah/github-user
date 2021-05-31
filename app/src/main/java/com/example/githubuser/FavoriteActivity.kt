package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.db.DbContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.db.DbHelper
import com.example.githubuser.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var db: DbHelper

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

        db = DbHelper(this)

        val cursor = contentResolver.query(CONTENT_URI,null,null,null,null)
        val users = db.parseCursorToArrayList(cursor!!)
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