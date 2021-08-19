package mjc.woo.internprojectkotlin.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mjc.woo.internprojectkotlin.other.CheckRateLimit
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
        binding.activity = this

        searchUserProcess()

        return view
    }

    private fun getSearchUsers(keyword: MutableSet<String>) {
        val callUserDetail = UserDetailRetrofitClient.userDetail
        val items = mutableListOf<SearchUserItem>()

        items.clear()

        val thread = Thread {
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

                if(adapter?.itemCount == null || adapter?.itemCount == 0){
                    binding.tvNoFavorites.visibility = View.VISIBLE
                    binding.favoritesRecyclerView.visibility = View.GONE
                }else{
                    binding.tvNoFavorites.visibility = View.GONE
                    binding.favoritesRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        thread.start()
        thread.join()

        binding.invalidateAll()
    }

    fun searchUserProcess() {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("key", 0)
        val set: MutableSet<String> = pref.all.keys

        if(CheckRateLimit().checkLimit(set.size)){
            getSearchUsers(set)
        } else{
            Toast.makeText(requireContext(), "검색 횟수가 부족합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (adapter != null)
            adapter?.notifyDataSetChanged()
    }

}

