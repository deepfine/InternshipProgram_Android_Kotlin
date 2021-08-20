package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.other.CheckRateLimit
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.activity.UserDetailActivity
import mjc.woo.internprojectkotlin.databinding.ListFollowersBinding
import mjc.woo.internprojectkotlin.item.FollowersItem

class FollowersRvAdapter(
    private val items: MutableList<FollowersItem>,
    private val activity: Activity
) : RecyclerView.Adapter<FollowersRvAdapter.Holder>() {
    private val pref: SharedPreferences = activity.getSharedPreferences("key", 0)
    private val editor: SharedPreferences.Editor = pref.edit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ListFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], activity)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(val binding: ListFollowersBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FollowersItem, context: Context) {
            binding.apply {
                tvId.text = item.userID
                Glide.with(context).load(item.userImgURL).into(profileImgview)

                if (pref.getBoolean(item.userID, false))
                    btnFavorites.setImageResource(R.drawable.favorites_start_check)
                else
                    btnFavorites.setImageResource(R.drawable.favorites_start_none)

                btnFavorites.setOnClickListener {
                    if (pref.getBoolean(item.userID, false)) {
                        btnFavorites.setImageResource(R.drawable.favorites_start_none)
                        editor.remove(item.userID)
                    } else {
                        btnFavorites.setImageResource(R.drawable.favorites_start_check)
                        editor.putBoolean(item.userID, true)
                    }
                    editor.apply()
                }
            }

            itemView.setOnClickListener {
                if (CheckRateLimit().checkLimit(2)) {
                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra("userId", item.userID)
                    activity.startActivity(intent)
                } else {
                    Toast.makeText(activity, "검색 횟수가 부족합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}