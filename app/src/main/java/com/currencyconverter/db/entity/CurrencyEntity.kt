package com.currencyconverter.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    var currencyName: String = "",
    @PrimaryKey
    var currencySymbol: String = ""
) {
    override fun toString(): String {
        return currencyName
    }
}
