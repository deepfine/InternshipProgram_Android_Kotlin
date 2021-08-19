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
    private var fragmentA: Fragment = SearchUserFragment()
    private var fragmentB: Fragment = FavoritesFragment()
    private var check: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val fragmentTranction = supportFragmentManager.beginTransaction()
        fragmentTranction.add(R.id.frameLayout, fragmentA)
        fragmentTranction.commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_SearchUser -> {
                    if(!check){
                        val fragmentTranction1 = supportFragmentManager.beginTransaction()
                        fragmentTranction1.show(fragmentA)
                        fragmentA.onStart()
                        fragmentTranction1.remove(fragmentB)
                        fragmentTranction1.commit()
                        check = !check
                        return@setOnItemSelectedListener true
                    }else{
                        return@setOnItemSelectedListener true
                    }
                }
                R.id.page_Favorites -> {
                    if(check){
                        val fragmentTranction1 = supportFragmentManager.beginTransaction()
                        fragmentTranction1.add(R.id.frameLayout, fragmentB)
                        fragmentTranction1.hide(fragmentA)
                        fragmentTranction1.commit()
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