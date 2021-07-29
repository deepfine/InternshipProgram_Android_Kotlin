package mjc.woo.internprojectkotlin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.activity.UserDetailActivity
import mjc.woo.internprojectkotlin.api.SearchUsersRetrofitClient
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.databinding.FragmentSearchUserBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem
import mjc.woo.internprojectkotlin.jsonclass.Item
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON
import mjc.woo.internprojectkotlin.jsonclass.UsersListJSON
import mjc.woo.internprojectkotlin.adapter.SearchUserListAdapter

class SearchUserFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding
    private var adapter: SearchUserListAdapter? = null
    private var items = mutableListOf<SearchUserItem>()

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
            val keyword: String = binding.searchKeyWordEdt.text.toString()
            if (keyword == ""){
                Toast.makeText(context, "검색할 아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else{
                getSearchUsers(keyword)
            }
        }

        binding.searchUserList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra("userId", adapter!!.getItem(position))
            startActivity(intent)
        }

        return view
    }

    private fun getSearchUsers(keyword: String) {
        val callUserSearch = SearchUsersRetrofitClient.searchUsers
        val callUserDetail = UserDetailRetrofitClient.userDetail

        items.clear()

        Thread {
            val users: UsersListJSON =
                callUserSearch.getPost(keyword, maxSearchCount).execute().body()!!
            val userItem: List<Item> = users.items


            for (i in userItem.indices) {
                val usersData: UserDetailJSON =
                    callUserDetail.getPost(userItem[i].login).execute().body()!!
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
                adapter = SearchUserListAdapter(items, requireActivity())
                binding.searchUserList.adapter = adapter
            }


        }.start()
    }

    override fun onResume() {
        super.onResume()

        if (adapter != null)
            adapter!!.notifyDataSetChanged()
    }
}
