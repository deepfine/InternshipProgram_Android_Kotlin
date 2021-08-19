package mjc.woo.internprojectkotlin.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.other.UserDetailText
import mjc.woo.internprojectkotlin.adapter.FollowersRvAdapter
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
    private var adapter: FollowersRvAdapter? = null

    private var userID: String? = null
    var srcId: Int? = null
    var followerVisibility: Int? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_userdetail)
        binding.activity = this

        pref = getSharedPreferences("key", 0)
        editor = pref.edit()

        userID = intent.getStringExtra("userId")
        userID?.let { getUserDetail(it) }
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
                    usersData.company?:getString(R.string.noData),
                    usersData.email?:getString(R.string.noData),
                    usersData.followers,
                    usersData.following,
                    this
                )

                adapter = FollowersRvAdapter(items, this)
                binding.followersRecyclerView.adapter = adapter


                val lm = LinearLayoutManager(applicationContext)
                binding.followersRecyclerView.layoutManager = lm
                binding.followersRecyclerView.setHasFixedSize(true)

                if (Integer.parseInt(usersData.followers) > 0) {
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

    companion object {
        @JvmStatic
        @BindingAdapter("imgSrc")
        fun changeFavoriteImage(view: ImageView, srcId: Int) {
            view.setImageResource(srcId)
        }
    }
}