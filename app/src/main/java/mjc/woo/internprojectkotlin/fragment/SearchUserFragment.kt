package mjc.woo.internprojectkotlin.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.activity.MainActivity

class SearchUserFragment : Fragment() {
    private lateinit var binding: SearchUserFragment
    // 최대 검색 유저 수
    val MAX_COUNT = 5
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

}