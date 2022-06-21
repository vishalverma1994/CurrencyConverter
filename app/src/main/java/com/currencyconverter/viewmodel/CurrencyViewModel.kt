package com.currencyconverter.viewmodel

import androidx.lifecycle.*
import com.currencyconverter.db.entity.CurrencyEntity
import com.currencyconverter.model.ConvertedRates
import com.currencyconverter.repository.CurrencyConverterRepository
import com.currencyconverter.utils.Constants
import com.currencyconverter.utils.NetworkHelper
import com.currencyconverter.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _convertCurrenciesLiveData = MutableLiveData<Resource<List<ConvertedRates>>>()
    val convertCurrenciesLiveData: LiveData<Resource<List<ConvertedRates>>> get() = _convertCurrenciesLiveData

    val currencies = repository.fetchCurrenciesFromDB()

    fun convertCurrencies(base: String, amount: String) = viewModelScope.launch {
        if (networkHelper.isNetworkConnected()) {
            _convertCurrenciesLiveData.postValue(Resource.loading(null))
            repository.convertCurrencies(Constants.APP_ID, base).let { response ->
                //After getting the response separating keys and values and manipulating the data
                //by converting the currencies on a given amount from api.
                if (response.isSuccessful) {
                    response.body().let { currencyConverterResponse ->
                        val targetJson = JSONObject(Gson().toJson(currencyConverterResponse?.rates))
                        val keys = targetJson.keys()
                        val list = mutableListOf<ConvertedRates>()
                        while (keys.hasNext()) {
                            val convertedRates = ConvertedRates()
                            convertedRates.currencySymbol = keys.next()
                            //amount conversion
                            convertedRates.currencyRates = (targetJson.optDouble(convertedRates.currencySymbol) * amount.toDouble())
                            list.add(convertedRates)
                        }
                        _convertCurrenciesLiveData.postValue(Resource.success(list))
                    }
                } else _convertCurrenciesLiveData.postValue(Resource.error(response.errorBody().toString(), null))
            }
        } else _convertCurrenciesLiveData.postValue(Resource.error(Constants.INTERNET_CONNECTION_ERROR_MESSAGE, null))
    }
}