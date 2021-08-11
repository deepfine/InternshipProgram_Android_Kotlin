package mjc.woo.internprojectkotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mjc.woo.internprojectkotlin.api.RateLimitRetrofitClient
import mjc.woo.internprojectkotlin.jsonclass.RateLimitJSON

class SearchUserFragmentViewModel: ViewModel() {
    var limit : MutableLiveData<String> = MutableLiveData()

    fun init(){
        getLimit()
    }

    fun getLimit() {
        val callRateLimit = RateLimitRetrofitClient.rateLimit

        Thread {
            val rateLimit: RateLimitJSON =
                callRateLimit.getPost().execute().body()!!

            limit.postValue("남은 검색 횟수: ${rateLimit.resources.core.remaining}")
        }.start()
    }

}