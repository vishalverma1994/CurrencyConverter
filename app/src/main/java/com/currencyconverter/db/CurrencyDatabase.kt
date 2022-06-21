package com.currencyconverter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.currencyconverter.db.dao.CurrencyDao
import com.currencyconverter.db.entity.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1
)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
}
