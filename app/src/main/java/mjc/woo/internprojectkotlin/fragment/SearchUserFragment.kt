package mjc.woo.internprojectkotlin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.SearchUserFragmentViewModel
import mjc.woo.internprojectkotlin.api.SearchUsersRetrofitClient
import mjc.woo.internprojectkotlin.api.UserDetailRetrofitClient
import mjc.woo.internprojectkotlin.databinding.FragmentSearchUserBinding
import mjc.woo.internprojectkotlin.item.SearchUserItem
import mjc.woo.internprojectkotlin.jsonclass.Item
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON
import mjc.woo.internprojectkotlin.jsonclass.UsersListJSON
import mjc.woo.internprojectkotlin.adapter.SearchUserRvAdapter

class SearchUserFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding
    private var adapter: SearchUserRvAdapter? = null
    private var items = mutableListOf<SearchUserItem>()
    lateinit var viewModel: SearchUserFragmentViewModel

    var rateLimit: String? = null

    // 최대 검색 유저 수
    private val maxSearchCount = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false)
        val view: View = binding.root
        binding.activity = this

        viewModel = ViewModelProvider(this).get(SearchUserFragmentViewModel::class.java)

        viewModel.init()

        viewModel.limit.observe(viewLifecycleOwner, {
            rateLimit = it
            Log.e("wyh", it)
            binding.invalidateAll()
        })
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

            if (users.total_count == 0) {
                requireActivity().runOnUiThread {
                    Toast.makeText(context, "검색된 유저가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
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
                    adapter = SearchUserRvAdapter(items, requireActivity())
                    binding.userSearchRecyclerView.adapter = adapter

                    val lm = LinearLayoutManager(context)
                    binding.userSearchRecyclerView.layoutManager = lm
                    binding.userSearchRecyclerView.setHasFixedSize(true)

                    viewModel.getLimit()
                }
            }
        }.start()
    }

    fun clickSearchBtn() {
        // 키보드 내리기
        val manager: InputMethodManager =
            activity?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)

        // EditText에 있는 내용 가져와서 문자열 검사
        val keyword: String = binding.searchKeyWordEdt.text.toString()
        if (keyword == "") {
            Toast.makeText(context, "검색할 아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            getSearchUsers(keyword)
        }
    }

    override fun onStart() {
        super.onStart()

        adapter?.notifyDataSetChanged()
    }
}
