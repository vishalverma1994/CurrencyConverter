package com.currencyconverter.api

import com.currencyconverter.model.CurrencyConverterResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {

    override suspend fun fetchCurrencies(): Response<Any> =
        apiService.fetchCurrencies()

    override suspend fun convertCurrencies(appId: String, base: String): Response<CurrencyConverterResponse> =
        apiService.convertCurrencies(appId, base)

}