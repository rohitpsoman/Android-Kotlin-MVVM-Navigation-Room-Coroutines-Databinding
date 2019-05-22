package com.example.mvvm.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Intercept all apis calls via Retrofit
 */

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            addHeader("Content-Type", "application/json")
            addHeader("Accept", "application/vnd.github.v3+json")
        }.build()

        return chain.proceed(request)
    }
}