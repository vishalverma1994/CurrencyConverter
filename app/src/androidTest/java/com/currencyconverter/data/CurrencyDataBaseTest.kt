package com.currencyconverter.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.currencyconverter.db.CurrencyDatabase
import com.currencyconverter.db.dao.CurrencyDao
import com.currencyconverter.db.entity.CurrencyEntity
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
class CurrencyDataBaseTest: TestCase() {

    private lateinit var currencyDatabase: CurrencyDatabase
    private lateinit var currencyDao: CurrencyDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        currencyDatabase = Room.inMemoryDatabaseBuilder(context, CurrencyDatabase::class.java).build()
        currencyDao = currencyDatabase.getCurrencyDao()
    }

    @After
    fun closeDataBase() {
        currencyDatabase.close()
    }

    @Test
    fun writeAndReadCurrency() = runBlocking {
        val currencyEntity = setCurrencyEntity()
        currencyDao.insertCurrencies(currencyEntity)
        val currencyList = currencyDao.getTestCurrencies()
        assertThat(currencyList.contains(currencyEntity)).isTrue()
    }

    private fun setCurrencyEntity(): CurrencyEntity {
        val currencyEntity = CurrencyEntity()
        currencyEntity.currencySymbol = "DUMMY"
        currencyEntity.currencyName = "DUMMY_NAME"
        return currencyEntity
    }
}