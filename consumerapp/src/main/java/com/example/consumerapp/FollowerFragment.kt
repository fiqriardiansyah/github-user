package com.example.consumerapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapp.adapter.UsersAdapter
import com.example.consumerapp.databinding.FragmentFollowerBinding
import com.example.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception


class FollowerFragment : Fragment() {

    private lateinit var progress: ProgressBar
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersAdapter = UsersAdapter()

        progress = view.findViewById(R.id.progressBarFollower)
        progress.visibility = View.VISIBLE

        val rvFollower = view.findViewById(R.id.rvFollower) as RecyclerView
        rvFollower.setHasFixedSize(true)
        rvFollower.layoutManager = LinearLayoutManager(context)

        val token = "8c011b5eb263ab260146eb799f96dc519d2e3da3"
        val url = "https://api.github.com/users/${DetailActivity.username}/followers"

        val client = AsyncHttpClient()

        client.addHeader("Authorization","token $token")
        client.addHeader("User-Agent","request")

        client.get(url,object: AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                progress.visibility = View.INVISIBLE

                val listUser = ArrayList<User>()
                val result = responseBody?.let { String(it) }

                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val singleUser = jsonArray.getJSONObject(i)
                        val user = User()
                        user.username = singleUser.getString("login")
                        user.avatar = singleUser.getString("avatar_url")
                        listUser.add(user)
                    }


                    rvFollower.adapter = usersAdapter
                    usersAdapter.setData(listUser)
                    usersAdapter.notifyDataSetChanged()
                    usersAdapter.onClick(object: UsersAdapter.OnItemClickListener{
                        override fun onItemClick(user: User) {
                            Toast.makeText(context,user.username,Toast.LENGTH_SHORT).show()
                        }
                    })

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }

        })


    }
}