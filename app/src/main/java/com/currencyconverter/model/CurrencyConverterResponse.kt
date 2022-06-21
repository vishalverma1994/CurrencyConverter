package com.currencyconverter.model

data class CurrencyConverterResponse(
    val disclaimer: String = "",
    val license: String = "",
    val timestamp: Long = 0,
    val rates: Any? = null
)

data class ConvertedRates(
    var currencySymbol: String = "",
    var currencyRates: Double = 0.0
)
