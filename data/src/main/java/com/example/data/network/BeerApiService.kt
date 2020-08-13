package com.example.data.network

import com.example.data.BuildConfig
import com.example.data.network.DTOs.NetworkBeer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val interceptor  = HttpLoggingInterceptor()

interface BeerApiService {
    @GET("beers")
    fun getBeers(@QueryMap filter: Map<String, String?>) : Deferred<List<NetworkBeer>>

    @GET("beers/random")
    fun getRandomBeer(): Deferred<List<NetworkBeer>>

    companion object {
        fun getService(): BeerApiService {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
            return retrofit.create(BeerApiService::class.java)
        }
    }
}