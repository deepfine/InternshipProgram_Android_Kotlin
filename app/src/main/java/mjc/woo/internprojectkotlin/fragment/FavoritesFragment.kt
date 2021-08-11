package mjc.woo.internprojectkotlin.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.adapter.FavoritesRvAdapter
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.databinding.FragmentFavoritesBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private lateinit var binding: FragmentFavoritesBinding
    private var adapter: FavoritesRvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        val view: View = binding.root

        val pref: SharedPreferences = requireActivity().getSharedPreferences("key", 0)

        val set: MutableSet<String> = pref.all.keys

        getSearchUsers(set)

        return view
    }

    private fun getSearchUsers(keyword: MutableSet<String>) {
        val callUserDetail = UserDetailRetrofitClient.userDetail
        val items = mutableListOf<SearchUserItem>()

        items.clear()

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
                adapter = FavoritesRvAdapter(items, requireActivity())
                binding.favoritesRecyclerView.adapter = adapter

                val lm = LinearLayoutManager(activity)
                binding.favoritesRecyclerView.layoutManager = lm
                binding.favoritesRecyclerView.setHasFixedSize(true)
            }

        }.start()
    }

    override fun onResume() {
        super.onResume()

        if (adapter != null)
            adapter?.notifyDataSetChanged()
    }
}

