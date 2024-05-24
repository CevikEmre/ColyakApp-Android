import com.example.colyak.viewmodel.loginResponse

import okhttp3.Interceptor
import okhttp3.Response


object AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val modifiedRequest = if (url.contains("/api/users/")) {
            request.newBuilder().build()
        } else {
            request.newBuilder()
                .addHeader("Authorization", "Bearer ${loginResponse.token}")
                .build()
        }

        return chain.proceed(modifiedRequest)
    }
}