package com.currencyconverter.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.currencyconverter.db.entity.CurrencyEntity
import com.currencyconverter.repository.CurrencyConverterRepository
import com.currencyconverter.utils.Resource
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.internal.aggregatedroot.AggregatedRoot
import org.json.JSONObject
import javax.inject.Inject

@HiltWorker
class CurrencyWorkManager @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject lateinit var currencyConverterRepository: CurrencyConverterRepository

    override suspend fun doWork(): Result {
        currencyConverterRepository.fetchCurrencies().let { response ->
            if (response.isSuccessful){
                //After getting response separating the keys and value, storing them in DB.
                response.body()?.let { currencyJsonObject->
                    val targetJson = JSONObject(Gson().toJson(currencyJsonObject))
                    val keys = targetJson.keys()
                    while (keys.hasNext()) {
                        val currencyEntity = CurrencyEntity()
                        currencyEntity.currencySymbol = keys.next()
                        currencyEntity.currencyName = targetJson.optString(currencyEntity.currencySymbol)
                        currencyConverterRepository.insertCurrencies(currencyEntity)
                    }
                }
            }
        }
        Log.e("Worker", "Inside Work Manager")
        return Result.success()
    }
}