package mjc.woo.internprojectkotlin.api

import mjc.woo.internprojectkotlin.jsonclass.RateLimitJSON
import mjc.woo.internprojectkotlin.jsonclass.UserDetailJSON
import mjc.woo.internprojectkotlin.jsonclass.UserFollowersJSON
import mjc.woo.internprojectkotlin.jsonclass.UsersListJSON
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class GitHubApi {
    companion object {
        const val DOMAIN = "https://api.github.com/"
    }
}

interface SearchUsers {
    @Headers("Authorization: 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")
    @GET("search/users")
    fun getPost(@Query("q") keyword: String, @Query("per_page") post2: Int): Call<UsersListJSON>
}

interface UserData {
    @Headers("Authorization: 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")
    @GET("users/{id}")
    fun getPost(@Path("id") keyword: String): Call<UserDetailJSON>
}

interface UserFollower {
    @Headers("Authorization: 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")
    @GET("users/{id}/followers")
    fun getPost(@Path("id") keyword: String): Call<UserFollowersJSON>
}

interface RateLimit {
    @Headers("Authorization: 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")
    @GET("rate_limit")
    fun getPost(): Call<RateLimitJSON>
}

object SearchUsersRetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val searchUsers: SearchUsers by lazy {
        retrofitClient.build().create(SearchUsers::class.java)
    }
}

object UserDetailRetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val userDetail: UserData by lazy {
        retrofitClient.build().create(UserData::class.java)
    }
}

object UserFollowersRetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val userFollower: UserFollower by lazy {
        retrofitClient.build().create(UserFollower::class.java)
    }
}

object RateLimitRetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(GitHubApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
    }
    val rateLimit: RateLimit by lazy {
        retrofitClient.build().create(RateLimit::class.java)
    }
}