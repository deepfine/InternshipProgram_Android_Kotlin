package mjc.woo.internprojectkotlin.adapter

import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.databinding.ListFollowersBinding
import mjc.woo.internprojectkotlin.item.FollowersItem

class FollowersListAdapter(
    private val items: MutableList<FollowersItem>,
    private val activity: Activity
) : BaseAdapter() {
    private lateinit var binding: ListFollowersBinding
    private val pref: SharedPreferences = activity.getSharedPreferences("key", 0)
    private val editor: SharedPreferences.Editor = pref.edit()


    override fun getCount(): Int = items.size

    override fun getItem(position: Int): String = items[position].userID

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view

        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.list_followers,
                parent,
                false
            )
            convertView = binding.root
        }

        val item: FollowersItem = items[position]

        Glide.with(activity).load(item.userImgURL).into(binding.profileImgview)
        binding.tvId.text = item.userID

        if (pref.getBoolean(item.userID, false))
            binding.btnFavorites.setImageResource(R.drawable.favorites_start_check)
        else
            binding.btnFavorites.setImageResource(R.drawable.favorites_start_none)

        binding.btnFavorites.setOnClickListener {
            if (pref.getBoolean(item.userID, false)) {
                binding.btnFavorites.setImageResource(R.drawable.favorites_start_none)
                editor.putBoolean(item.userID, false)
                editor.remove(item.userID)
            } else {
                binding.btnFavorites.setImageResource(R.drawable.favorites_start_check)
                editor.putBoolean(item.userID, true)
            }
            editor.apply()
        }


        return convertView
    }
}