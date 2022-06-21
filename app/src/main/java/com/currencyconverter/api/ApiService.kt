package com.currencyconverter.api

import com.currencyconverter.model.CurrencyConverterResponse
import com.currencyconverter.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.FETCH_CURRENCIES)
    suspend fun fetchCurrencies(): Response<Any>

    @GET(Constants.CONVERT_CURRENCIES)
    suspend fun convertCurrencies(
        @Query("app_id") appId: String,
        @Query("base") base: String
    ): Response<CurrencyConverterResponse>
}