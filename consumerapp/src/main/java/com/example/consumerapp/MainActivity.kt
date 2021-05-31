package com.example.consumerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.UsersAdapter
import com.example.consumerapp.databinding.ActivityMainBinding
import com.example.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer app"

        adapter = UsersAdapter()
        binding.rvContainer.setHasFixedSize(true)
        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = adapter

        getUser("fiqri ardiansyah");

        binding.edtSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getUser((if(text?.isEmpty() == true) "fiqri ardiansyah" else text).toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val mIntent = Intent(this@MainActivity,FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return true
    }

    private fun setData(users: ArrayList<User>){
        adapter.setData(users)
        adapter.notifyDataSetChanged()
        adapter.onClick(object: UsersAdapter.OnItemClickListener{
            override fun onItemClick(user: User) {
                val mIntent = Intent(this@MainActivity,DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USER,user)
                startActivity(mIntent)
            }
        })
    }

    private fun getUser(username: String?){
        binding.progressBar.visibility = View.VISIBLE
        val token = "8c011b5eb263ab260146eb799f96dc519d2e3da3"
        val url = "https://api.github.com/search/users?q=${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","token $token")
        client.addHeader("User-Agent","request")

        client.get(url,object: AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {

                binding.progressBar.visibility = View.INVISIBLE

                val users = ArrayList<User>()
                val result = responseBody?.let { String(it) }

                try{

                    val json = JSONObject(result).getJSONArray("items")

                    for(i in 0 until json.length()){
                        val singleUser = json.getJSONObject(i)
                        val user = User()
                        user.username = singleUser.getString("login")
                        user.avatar = singleUser.getString("avatar_url")
                        users.add(user)
                    }

                    setData(users)

                }catch (e: Exception){
                    e.printStackTrace()
                    Log.e("error",e.toString())
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Toast.makeText(this@MainActivity,"oops something went wrong!!",Toast.LENGTH_SHORT).show()
            }

        })
    }
}