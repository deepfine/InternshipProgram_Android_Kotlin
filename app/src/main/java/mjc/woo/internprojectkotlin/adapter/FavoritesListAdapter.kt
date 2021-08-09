package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.item.SearchUserItem

class FavoritesListAdapter(
    private val items: MutableList<SearchUserItem>,
    private val activity: Activity
):BaseAdapter() {
    private val pref: SharedPreferences = activity.getSharedPreferences("key", 0)
    private val editor: SharedPreferences.Editor = pref.edit()


    override fun getCount(): Int = items.size

    override fun getItem(position: Int): String = items[position].userID

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_main, parent, false)

            holder = ViewHolder()
            holder.favBtn = view.findViewById(R.id.btn_favorites)
            holder.userId = view.findViewById(R.id.tv_id)
            holder.userImg = view.findViewById(R.id.profile_imgview)
            holder.userEmail = view.findViewById(R.id.tv_email)
            holder.userCompany = view.findViewById(R.id.tv_company)
            holder.userName = view.findViewById(R.id.tv_name)

            view.tag = holder
        } else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item: SearchUserItem = items[position]

        Glide.with(activity).load(item.userImgURL).into(holder.userImg)
        holder.userId?.text = item.userID
        holder.userName?.text = activity.getString(R.string.name).plus(item.userName)
        holder.userEmail?.text = activity.getString(R.string.email).plus(item.userEmail)
        holder.userCompany?.text = activity.getString(R.string.company).plus(item.userCompany)

        if (pref.getBoolean(item.userID, false))
            holder.favBtn?.setImageResource(R.drawable.favorites_start_check)
        else
            holder.favBtn?.setImageResource(R.drawable.favorites_start_none)

        holder.favBtn?.setOnClickListener {
            if (pref.getBoolean(item.userID, false)) {
                holder.favBtn?.setImageResource(R.drawable.favorites_start_none)
                editor.putBoolean(item.userID, false)
                editor.remove(item.userID)
            } else {
                holder.favBtn?.setImageResource(R.drawable.favorites_start_check)
                editor.putBoolean(item.userID, true)
            }
            editor.apply()
        }


        return view
    }
}