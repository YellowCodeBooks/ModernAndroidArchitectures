package com.yellowcode.modernandroidarchitecruesdemo.networking

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object CountriesService {

    private val BASE_URL = "https://restcountries.com/v3.1/"

    fun create(): CountriesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(CountriesApi::class.java)
    }
}