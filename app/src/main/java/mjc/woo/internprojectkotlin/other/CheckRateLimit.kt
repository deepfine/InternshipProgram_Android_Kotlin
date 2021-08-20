package mjc.woo.internprojectkotlin.other

import android.util.Log
import mjc.woo.internprojectkotlin.api.RateLimitRetrofitClient
import mjc.woo.internprojectkotlin.jsonclass.RateLimitJSON

class CheckRateLimit {
    private lateinit var remaining: String
    fun checkLimit(needLimit: Int): Boolean{
        val callRateLimit = RateLimitRetrofitClient.rateLimit
        val thread = Thread {
            val rateLimit: RateLimitJSON =
                callRateLimit.getPost().execute().body()!!

            remaining = rateLimit.resources.core.remaining
            Log.e("wyh", remaining)
            Log.e("wyh", needLimit.toString())
        }

        thread.start()
        thread.join()

        return Integer.parseInt(remaining) >= needLimit
    }
}