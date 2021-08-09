package mjc.woo.internprojectkotlin.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.UserDetailText
import mjc.woo.internprojectkotlin.adapter.FollowersListAdapter
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.api.UserFollowersRetrofitClient
import mjc.woo.internprojectkotlin.databinding.ActivityUserdetailBinding
import mjc.woo.internprojectkotlin.item.FollowersItem
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON
import mjc.woo.internprojectkotlin.jsonclass.UserFollowersJSON

class UserDetailActivity : AppCompatActivity() {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: ActivityUserdetailBinding
    private var items = mutableListOf<FollowersItem>()
    private var adapter: FollowersListAdapter? = null

    var userID: String? = null
    var srcId: Int? = null
    var followerVisibility: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_userdetail)
        binding.activity = this

        pref = getSharedPreferences("key", 0)
        editor = pref.edit()

        userID = intent.getStringExtra("userId")
        userID?.let { getUserDetail(it) }

        binding.followersUserList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("userId", adapter?.getItem(position))
            startActivity(intent)
        }
    }

    private fun getUserDetail(userID: String) {
        val callUserDetail = UserDetailRetrofitClient.userDetail
        val callUserFollower = UserFollowersRetrofitClient.userFollower

        srcId = if (pref.getBoolean(userID, false))
            R.drawable.favorites_start_check
        else
            R.drawable.favorites_start_none

        Thread {
            val usersData: UserDetailJSON =
                callUserDetail.getPost(userID).execute().body()!!
            val userFollowers: UserFollowersJSON =
                callUserFollower.getPost(userID).execute().body()!!

            for (i in userFollowers.indices) {
                items.add(
                    FollowersItem(userFollowers[i].login, userFollowers[i].avatar_url)
                )
            }

            runOnUiThread {
                Glide.with(applicationContext).load(usersData.avatar_url)
                    .into(binding.profileImgview)

                binding.userData = UserDetailText(
                    usersData.login,
                    usersData.name,
                    usersData.company,
                    usersData.email,
                    usersData.followers,
                    usersData.following,
                    this
                    )

                adapter = FollowersListAdapter(items, this)
                binding.followersUserList.adapter = adapter

                if(Integer.parseInt(usersData.followers) > 0){
                    followerVisibility = View.GONE
                }

                binding.invalidateAll()
            }

        }.start()
    }

    fun favoriteBtnClick() {
        if (pref.getBoolean(userID, false)) {
            binding.btnFavorites.setImageResource(R.drawable.favorites_start_none)
            editor.putBoolean(userID, false)
            editor.remove(userID)
        } else {
            binding.btnFavorites.setImageResource(R.drawable.favorites_start_check)
            editor.putBoolean(userID, true)
        }
        editor.apply()
    }
}