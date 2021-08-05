package mjc.woo.internprojectkotlin.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.activity.UserDetailActivity
import mjc.woo.internprojectkotlin.adapter.FavoritesListAdapter
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.databinding.FragmentFavoritesBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private lateinit var binding: FragmentFavoritesBinding
    private var adapter: FavoritesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        val view: View = binding.root

        val pref: SharedPreferences = this.requireActivity().getSharedPreferences("key", 0)

        val set: MutableSet<String> = pref.all.keys

        getSearchUsers(set)

        binding.favoritesList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), UserDetailActivity::class.java)
            intent.putExtra("userId", adapter?.getItem(position))
            startActivity(intent)
        }

        return view
    }

    private fun getSearchUsers(keyword: MutableSet<String>) {
        val callUserDetail = UserDetailRetrofitClient.userDetail
        val items = mutableListOf<SearchUserItem>()

        Thread {
            for (i in 0 until keyword.size step 1) {
                val usersData: UserDetailJSON =
                    callUserDetail.getPost(keyword.elementAt(i)).execute().body()!!
                items.add(
                    SearchUserItem(
                        usersData.login,
                        usersData.avatar_url,
                        usersData.name ?: "-",
                        usersData.email ?: "-",
                        usersData.company ?: "-"
                    )
                )
            }
            requireActivity().runOnUiThread {
                adapter = FavoritesListAdapter(items, requireActivity())
                binding.favoritesList.adapter = adapter
            }

        }.start()
    }

    override fun onResume() {
        super.onResume()

        if (adapter != null)
            adapter?.notifyDataSetChanged()
    }
}

