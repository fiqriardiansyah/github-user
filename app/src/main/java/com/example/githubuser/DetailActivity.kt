package com.example.githubuser

import android.content.ContentValues
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import androidx.viewpager2.widget.ViewPager2
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_AVATAR
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_COMPANY
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWERS
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWER_URL
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWINGS
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWING_URL
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_LOCATION
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_NAME
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_REPOSITORY
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_USERNAME
import com.example.githubuser.db.DbContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.db.DbHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: User
    private lateinit var db: DbHelper

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = ""
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        private var followTotal : ArrayList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = user.username

        username = user.username

        getUser(user.username)
        viewPager(followTotal)

        db = DbHelper(this)

        val uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + "${user.username}")
        val cursor = contentResolver.query(uriWithUsername,null,null,null,null)
        if(cursor?.count == 0){
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }else{
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }

        binding.fabFavorite.setOnClickListener {
            val uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + "${user.username}")
            val cursor = contentResolver.query(uriWithUsername,null,null,null,null)
            if(cursor?.count == 0){

                val values = ContentValues()
                values.put(COLUMN_NAME,user.name)
                values.put(COLUMN_USERNAME,user.username)
                values.put(COLUMN_AVATAR,user.avatar)
                values.put(COLUMN_COMPANY,user.company)
                values.put(COLUMN_LOCATION,user.location)
                values.put(COLUMN_FOLLOWER_URL,user.followerUrl)
                values.put(COLUMN_FOLLOWING_URL,user.followingUrl)
                values.put(COLUMN_FOLLOWERS,user.followers)
                values.put(COLUMN_FOLLOWINGS,user.following)
                values.put(COLUMN_REPOSITORY,user.repository)

                val insert = contentResolver.insert(CONTENT_URI,values)
                Toast.makeText(applicationContext,"berhasil ditambah",Toast.LENGTH_SHORT).show()
                binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)

            }else{
                val delete = contentResolver.delete(uriWithUsername,null,null)
                if(delete == -1){
                    Toast.makeText(applicationContext,"gagal dihapus",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,"berhasil dihapus",Toast.LENGTH_SHORT).show()
                    binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        }

    }

    private fun viewPager(followTotal: ArrayList<String>){
        val sectionsPagesAdapter = SectionsPagesAdapter(this)
        binding.viewPager.adapter = sectionsPagesAdapter
        TabLayoutMediator(binding.tabs,binding.viewPager)   {tab,position ->
            if(followTotal.size != 0){
                tab.text = resources.getString(TAB_TITLES[position],followTotal[position])
            }else{
                tab.text = resources.getString(TAB_TITLES[position])
            }
        }.attach()
    }

    private fun getUser(username: String){

        val token = "8c011b5eb263ab260146eb799f96dc519d2e3da3"
        val url = "https://api.github.com/users/${username}"

        val client = AsyncHttpClient()

        client.addHeader("Authorization","token $token")
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val response = responseBody?.let { String(it) }
                try {
                    val getUser = JSONObject(response)

                    Glide.with(this@DetailActivity)
                        .load(getUser.getString("avatar_url"))
                        .into(binding.profileImageDetail)

                    binding.nameDetail.text = getUser.getString("name")
                    binding.companyDetail.text = getUser.getString("company") ?: ""
                    binding.locationDetail.text = getUser.getString("location")
                    binding.repositoryDetail.text = getUser.getInt("public_repos").toString()

                    followTotal.clear()
                    followTotal.add(getUser.getInt("followers").toString())
                    followTotal.add(getUser.getInt("following").toString())

                    user.name = getUser.getString("name") ?: ""
                    user.avatar = getUser.getString("avatar_url") ?: ""
                    user.company = getUser.getString("company") ?: ""
                    user.followers = getUser.getInt("followers")
                    user.following = getUser.getInt("following")
                    user.location = getUser.getString("location")
                    user.repository = getUser.getInt("public_repos")
                    user.username = user.username

                    viewPager(followTotal)

                }catch (e: Exception){
                    Log.e("error",e.message.toString())
                    Toast.makeText(this@DetailActivity,e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Toast.makeText(this@DetailActivity,error?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}