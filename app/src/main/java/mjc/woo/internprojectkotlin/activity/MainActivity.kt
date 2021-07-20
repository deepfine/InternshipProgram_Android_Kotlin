package mjc.woo.internprojectkotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import mjc.woo.internprojectkotlin.R
import mjc.woo.internprojectkotlin.fragment.FavoritesFragment
import mjc.woo.internprojectkotlin.fragment.SearchUserFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView : NavigationBarView = findViewById(R.id.bottomNav)

        replaceFragment(SearchUserFragment())

        navigationView.setOnItemSelectedListener{
            when(it.itemId){
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

    private fun replaceFragment(fragment: Fragment){
        val fragmentTranction = supportFragmentManager.beginTransaction()
        fragmentTranction.replace(R.id.frameLayout, fragment)
        fragmentTranction.commit()
    }

}