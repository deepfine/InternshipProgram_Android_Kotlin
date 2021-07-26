package mjc.woo.internprojectkotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.databinding.ActivityMainBinding
import mjc.woo.internprojectkotlin.fragment.FavoritesFragment
import mjc.woo.internprojectkotlin.fragment.SearchUserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        replaceFragment(SearchUserFragment())


        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_SearchUser -> {
                    replaceFragment(SearchUserFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.page_Favorites -> {
                    replaceFragment(FavoritesFragment())
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTranction = supportFragmentManager.beginTransaction()
        fragmentTranction.replace(R.id.frameLayout, fragment)
        fragmentTranction.commit()
    }

}