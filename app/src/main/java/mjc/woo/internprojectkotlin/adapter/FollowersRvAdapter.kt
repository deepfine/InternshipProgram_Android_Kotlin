package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        val binding = ListFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            binding.tvId.text = item.userID
            Glide.with(context).load(item.userImgURL).into(binding.profileImgview)

            if (pref.getBoolean(item.userID, false))
                binding.btnFavorites.setImageResource(R.drawable.favorites_start_check)
            else
                binding.btnFavorites.setImageResource(R.drawable.favorites_start_none)

            binding.btnFavorites.setOnClickListener {
                if (pref.getBoolean(item.userID, false)) {
                    binding.btnFavorites.setImageResource(R.drawable.favorites_start_none)
                    editor.remove(item.userID)
                } else {
                    binding.btnFavorites.setImageResource(R.drawable.favorites_start_check)
                    editor.putBoolean(item.userID, true)
                }
                editor.apply()
            }

            itemView.setOnClickListener {
                val intent = Intent(context, UserDetailActivity::class.java)
                intent.putExtra("userId", item.userID)
                activity.startActivity(intent)
            }
        }
    }
}