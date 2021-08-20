package mjc.woo.internprojectkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mjc.woo.internprojectkotlin.api.RateLimitRetrofitClient
import mjc.woo.internprojectkotlin.jsonclass.RateLimitJSON

class SearchUserFragmentViewModel: ViewModel() {
    var limitString : MutableLiveData<String> = MutableLiveData()
    lateinit var limit :String

    fun init(){
        getLimit()
    }

    fun getLimit() {
        val callRateLimit = RateLimitRetrofitClient.rateLimit

        Thread {
            val rateLimit: RateLimitJSON =
                callRateLimit.getPost().execute().body()!!

            limit = rateLimit.resources.core.remaining

            limitString.postValue("남은 검색 횟수: $limit")
        }.start()
    }

}