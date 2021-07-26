package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.item.SearchUserItem

class SearchUserListAdapter(
    private val items: MutableList<SearchUserItem>,
    private val activity: Activity
) :
    BaseAdapter() {

    private val pref: SharedPreferences = activity.getPreferences(0)
    private val editor: SharedPreferences.Editor = pref.edit()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SearchUserItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view

        if (convertView == null) {
            convertView =
                LayoutInflater.from(parent?.context).inflate(R.layout.list_main, parent, false)
        }

        val item: SearchUserItem = items[position]

        val tvId = convertView?.findViewById<TextView>(R.id.tv_id)
        val imgUrl = convertView?.findViewById<ImageView>(R.id.profile_imgview)
        val tvName = convertView?.findViewById<TextView>(R.id.tv_name)
        val tvEmail = convertView?.findViewById<TextView>(R.id.tv_email)
        val tvCompany = convertView?.findViewById<TextView>(R.id.tv_company)
        val favoritesBtn = convertView?.findViewById<ImageView>(R.id.btn_favorites)

        Glide.with(activity).load(item.userImgURL).into(imgUrl)
        tvId!!.text = item.userID
        tvName!!.text = activity.getString(R.string.name).plus(item.userName)
        tvEmail!!.text = activity.getString(R.string.email).plus(item.userEmail)
        tvCompany!!.text = activity.getString(R.string.company).plus(item.userCompany)

        if (pref.getBoolean(item.userID, false))
            favoritesBtn!!.setImageResource(R.drawable.favorites_start_check)
        else
            favoritesBtn!!.setImageResource(R.drawable.favorites_start_none)

        favoritesBtn.setOnClickListener(View.OnClickListener {
            if (pref.getBoolean(item.userID, false)) {
                favoritesBtn.setImageResource(R.drawable.favorites_start_none)
                editor.putBoolean(item.userID, false)
                editor.remove(item.userID)
            } else {
                favoritesBtn.setImageResource(R.drawable.favorites_start_check)
                editor.putBoolean(item.userID, true)
            }
            editor.apply()
        })


        return convertView
    }
}