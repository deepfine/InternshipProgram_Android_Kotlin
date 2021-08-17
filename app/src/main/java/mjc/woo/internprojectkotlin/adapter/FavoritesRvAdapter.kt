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
import mjc.woo.internprojectkotlin.databinding.ListMainBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem

class FavoritesRvAdapter(
    private val items: MutableList<SearchUserItem>,
    private val activity: Activity
) : RecyclerView.Adapter<FavoritesRvAdapter.Holder>() {
    private val pref: SharedPreferences = activity.getSharedPreferences("key", 0)
    private val editor: SharedPreferences.Editor = pref.edit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], activity)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(val binding: ListMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchUserItem, context: Context) {
            binding.apply{
                tvId.text = item.userID
                tvName.text = activity.getString(R.string.name).plus(item.userName)
                tvCompany.text = activity.getString(R.string.company).plus(item.userCompany)
                tvEmail.text = activity.getString(R.string.email).plus(item.userEmail)
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
                val intent = Intent(context, UserDetailActivity::class.java)
                intent.putExtra("userId", item.userID)
                activity.startActivity(intent)
            }
        }
    }
}