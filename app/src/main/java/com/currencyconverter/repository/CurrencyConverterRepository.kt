package com.currencyconverter.repository

import com.currencyconverter.api.ApiHelper
import com.currencyconverter.db.dao.CurrencyDao
import com.currencyconverter.db.entity.CurrencyEntity
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val apiHelper: ApiHelper
) {

    suspend fun fetchCurrencies() = apiHelper.fetchCurrencies()

    suspend fun convertCurrencies(appId: String, base: String) = apiHelper.convertCurrencies(appId, base)


    //Data base
    suspend fun insertCurrencies(currencyEntity: CurrencyEntity) = currencyDao.insertCurrencies(currencyEntity)

    fun fetchCurrenciesFromDB() = currencyDao.getAllCurrencies()

    fun getCurrenciesCount() = currencyDao.getCurrenciesCount()
}