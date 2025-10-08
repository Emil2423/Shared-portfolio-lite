package com.emil.sharedportfoliolite.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface YahooFinanceApiService {
    @GET("v8/finance/chart/{symbol}")
    suspend fun getQuote(@Path("symbol") symbol: String): YahooFinanceResponse

    companion object {
        private const val BASE_URL = "https://query1.finance.yahoo.com/"

        fun create(): YahooFinanceApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YahooFinanceApiService::class.java)

        }
    }

}