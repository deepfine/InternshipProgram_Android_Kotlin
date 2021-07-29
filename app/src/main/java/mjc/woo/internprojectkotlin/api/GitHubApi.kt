package mjc.woo.internprojectkotlin.api

import mjc.woo.internprojectkotlin.jsonclass.UserDetail
import mjc.woo.internprojectkotlin.jsonclass.UsersListJSON
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class GitHubApi {
    companion object{
        const val DOMAIN = "https://api.github.com/"
    }
}

interface SearchUsers{
    @GET("search/users")
    fun getPost(@Query("q") keyword: String, @Query("per_page") post2: Int) : Call<UsersListJSON>
}

interface UserData{
    @GET("users/{id}")
    fun getPost(@Path("id") keyword: String) : Call<UserDetail>
}

object SearchUsersRetrofitClient{
    private val retrofitClient: Retrofit.Builder by lazy{
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val searchUsers: SearchUsers by lazy{
        retrofitClient.build().create(SearchUsers::class.java)
    }
}

object UserDetailRetrofitClient{
    private val retrofitClient: Retrofit.Builder by lazy{
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val userDetail: UserData by lazy{
        retrofitClient.build().create(UserData::class.java)
    }
}