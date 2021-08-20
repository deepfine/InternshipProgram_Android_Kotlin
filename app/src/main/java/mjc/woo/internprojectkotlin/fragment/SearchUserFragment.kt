package mjc.woo.internprojectkotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mjc.woo.internprojectkotlin.other.CheckRateLimit
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.viewmodel.SearchUserFragmentViewModel
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
    private var viewModel = SearchUserFragmentViewModel()
    private val maxSearchCount = 5

    var rateLimit: String? = null

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

        viewModel.limitString.observe(viewLifecycleOwner, {
            rateLimit = it
            binding.invalidateAll()
        })
        return view
    }

    private fun getSearchUsers(keyword: String, viewModel: SearchUserFragmentViewModel) {
        val callUserSearch = SearchUsersRetrofitClient.searchUsers
        val callUserDetail = UserDetailRetrofitClient.userDetail

        items.clear()

        Thread {
            val users: UsersListJSON =
                callUserSearch.getPost(keyword, maxSearchCount).execute().body()!!
            val userItem: List<Item> = users.items

            if (users.total_count == 0) {
                requireActivity().runOnUiThread {
                    Toast.makeText(context, getString(R.string.suf_nothing_result), Toast.LENGTH_SHORT).show()
                }
            } else {
                val count: Int = users.items.size

                if (CheckRateLimit().checkLimit(count)) {
                    for (i in userItem.indices) {
                        val usersData: UserDetailJSON =
                            callUserDetail.getPost(userItem[i].login).execute().body()!!
                        items.add(
                            SearchUserItem(
                                userItem[i].login,
                                userItem[i].avatar_url,
                                usersData.name ?: getString(R.string.hyphen),
                                usersData.email ?: getString(R.string.hyphen),
                                usersData.company ?: getString(R.string.hyphen)
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
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), getString(R.string.noLimit), Toast.LENGTH_SHORT).show()
                    }

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
            Toast.makeText(context, getString(R.string.suf_input_keyword), Toast.LENGTH_SHORT).show()
        } else {
            getSearchUsers(keyword, viewModel)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.init()
        adapter?.notifyDataSetChanged()
    }
}
