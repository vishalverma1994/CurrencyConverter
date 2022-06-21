package com.currencyconverter.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.currencyconverter.db.entity.CurrencyEntity

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencyEntity: CurrencyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrency(currencyEntity: CurrencyEntity): Int

    @Query("Select * from currency")
    fun getAllCurrencies(): LiveData<List<CurrencyEntity>>

    @Query("SELECT * FROM currency")
    fun getTestCurrencies(): List<CurrencyEntity>

    @Query("SELECT COUNT(*) FROM currency")
    fun getCurrenciesCount(): LiveData<Int>

    @Delete
    suspend fun deleteCurrency(currencyEntity: CurrencyEntity)

    @Query("DELETE FROM currency WHERE currencySymbol = :currencySymbol")
    fun deleteCurrency(currencySymbol: String): Int
}