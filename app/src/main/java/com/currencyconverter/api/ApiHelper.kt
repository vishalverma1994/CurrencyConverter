package com.currencyconverter.api

import com.currencyconverter.model.CurrencyConverterResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun fetchCurrencies(): Response<Any>

    suspend fun convertCurrencies(appId: String, base: String): Response<CurrencyConverterResponse>
}