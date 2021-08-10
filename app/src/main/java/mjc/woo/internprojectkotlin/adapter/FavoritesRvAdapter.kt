package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.activity.UserDetailActivity
import mjc.woo.internprojectkotlin.item.SearchUserItem

class FavoritesRvAdapter(
    private val items: MutableList<SearchUserItem>,
    private val activity: Activity
) : RecyclerView.Adapter<FavoritesRvAdapter.Holder>() {
    private val pref: SharedPreferences = activity.getSharedPreferences("key", 0)
    private val editor: SharedPreferences.Editor = pref.edit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(activity).inflate(R.layout.list_main, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], activity)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userId: TextView = itemView.findViewById(R.id.tv_id)
        private val userName: TextView = itemView.findViewById(R.id.tv_name)
        private val userCompany: TextView = itemView.findViewById(R.id.tv_company)
        private val userEmail: TextView = itemView.findViewById(R.id.tv_email)
        private val favBtn: ImageView = itemView.findViewById(R.id.btn_favorites)
        private val userImg: ImageView = itemView.findViewById(R.id.profile_imgview)

        fun bind(item: SearchUserItem, context: Context) {
            userId.text = item.userID
            userName.text = activity.getString(R.string.name).plus(item.userName)
            userCompany.text = activity.getString(R.string.company).plus(item.userCompany)
            userEmail.text = activity.getString(R.string.email).plus(item.userEmail)
            Glide.with(context).load(item.userImgURL).into(userImg)

            if (pref.getBoolean(item.userID, false))
                favBtn.setImageResource(R.drawable.favorites_start_check)
            else
                favBtn.setImageResource(R.drawable.favorites_start_none)

            favBtn.setOnClickListener {
                if (pref.getBoolean(item.userID, false)) {
                    favBtn.setImageResource(R.drawable.favorites_start_none)
                    editor.remove(item.userID)
                } else {
                    favBtn.setImageResource(R.drawable.favorites_start_check)
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