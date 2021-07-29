package mjc.woo.internprojectkotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.api.SearchUsersRetrofitClient
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.databinding.FragmentSearchUserBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem
import mjc.woo.internprojectkotlin.jsonclass.Item
import mjc.woo.internprojectkotlin.jsonclass.UserDetail
import mjc.woo.internprojectkotlin.jsonclass.UsersListJSON
import mjc.woo.internprojectkotlin.adapter.SearchUserListAdapter

class SearchUserFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding

    // 최대 검색 유저 수
    private val maxSearchCount = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false)
        val view: View = binding.root

        binding.searchBtn.setOnClickListener {
            val keyword = binding.searchKeyWordEdt.text.toString()

            getSearchUsers(keyword)
        }
        return view
    }

    private fun getSearchUsers(keyword: String) {
        val callUserSearch = SearchUsersRetrofitClient.searchUsers
        val callUserDetail = UserDetailRetrofitClient.userDetail

        Thread {
            val users: UsersListJSON = callUserSearch.getPost(keyword, maxSearchCount).execute().body()!!
            val userItem: List<Item> = users.items
            val items = mutableListOf<SearchUserItem>()

            for (i in userItem.indices) {
                val usersData: UserDetail = callUserDetail.getPost(userItem[i].login).execute().body()!!
                items.add(
                    SearchUserItem(
                        userItem[i].login,
                        userItem[i].avatar_url,
                        usersData.name ?: "-",
                        usersData.email ?: "-",
                        usersData.company ?: "-"
                    )
                )
            }

            requireActivity().runOnUiThread {
                val adapter = SearchUserListAdapter(items, requireActivity())
                binding.searchUserList.adapter = adapter
            }


        }.start()
    }
}
