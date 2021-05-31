package com.example.githubuser

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
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentFollowingBinding
import com.example.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.lang.Exception

class FollowingFragment : Fragment() {

    private lateinit var progress: ProgressBar
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersAdapter = UsersAdapter()

        progress = view.findViewById(R.id.progressBarFollowing)
        progress.visibility = View.VISIBLE

        val rvFollowing = view.findViewById(R.id.rvFollowing) as RecyclerView
        rvFollowing.setHasFixedSize(true)
        rvFollowing.layoutManager = LinearLayoutManager(context)

        val token = "8c011b5eb263ab260146eb799f96dc519d2e3da3"
        val url = "https://api.github.com/users/${DetailActivity.username}/following"

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

                    rvFollowing.adapter = usersAdapter
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