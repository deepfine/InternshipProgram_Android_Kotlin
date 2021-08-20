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
    private var fragmentSearchUser: Fragment = SearchUserFragment()
    private var fragmentFavorites: Fragment = FavoritesFragment()
    private var check: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val transitionMain = supportFragmentManager.beginTransaction()
        transitionMain.add(R.id.frameLayout, fragmentSearchUser)
        transitionMain.commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_SearchUser -> {
                    if(!check){
                        val transitionSearchUser = supportFragmentManager.beginTransaction()
                        transitionSearchUser.show(fragmentSearchUser)
                        fragmentSearchUser.onStart()
                        transitionSearchUser.remove(fragmentFavorites)
                        transitionSearchUser.commit()
                        check = !check
                        return@setOnItemSelectedListener true
                    }else{
                        return@setOnItemSelectedListener true
                    }
                }
                R.id.page_Favorites -> {
                    if(check){
                        val transitionFavorites = supportFragmentManager.beginTransaction()
                        transitionFavorites.add(R.id.frameLayout, fragmentFavorites)
                        transitionFavorites.hide(fragmentSearchUser)
                        transitionFavorites.commit()
                        check = !check
                        return@setOnItemSelectedListener true
                    }else{
                        return@setOnItemSelectedListener true
                    }

                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

}