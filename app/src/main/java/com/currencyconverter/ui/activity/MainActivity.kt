package com.currencyconverter.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.currencyconverter.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.currencyconverter.databinding.ActivityMainBinding
import com.currencyconverter.db.entity.CurrencyEntity
import com.currencyconverter.ui.adapter.CurrencyConverterAdapter
import com.currencyconverter.utils.Status
import com.currencyconverter.viewmodel.CurrencyViewModel
import com.currencyconverter.workers.CurrencyWorkManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = MainActivity::class.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var currencyConverterAdapter: CurrencyConverterAdapter
    private val currencyViewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val currency = parent?.selectedItem as CurrencyEntity
        convertCurrency(currency.currencySymbol)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun initComponents() {
        observeCurrencyFinalLiveData()
        observeConvertCurrencyLiveData()
        fetchCurrencies()
        setCurrencyConversionsRecyclerView()
    }

    private fun fetchCurrencies() {
        val myPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<CurrencyWorkManager>(30, TimeUnit.MINUTES)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("currency_worker",ExistingPeriodicWorkPolicy.KEEP,  myPeriodicWorkRequest)
    }

    private fun setCurrencyConversionsRecyclerView() {
        binding.rvCurrencyConversions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            currencyConverterAdapter = CurrencyConverterAdapter()
            adapter = currencyConverterAdapter
        }
    }

    private fun observeCurrencyFinalLiveData() {
        currencyViewModel.currencies.observe(this) { currencyList ->
            currencyList?.let {
                if (it.isNotEmpty()) {
                    setSpinnerAdapter(it)
                    Log.e(TAG, Gson().toJson(it))
                }
            }
        }
    }

    private fun setSpinnerAdapter(currencyList: List<CurrencyEntity>) {
        val userAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        binding.spnCurrency.adapter = userAdapter
        binding.spnCurrency.onItemSelectedListener = this
    }

    private fun convertCurrency(currencySymbol: String) {
        var amount = getString(R.string._one)
        if (binding.etAmount.text.toString().isNotEmpty())
            amount = binding.etAmount.text.toString()
        currencyViewModel.convertCurrencies(currencySymbol, amount)
    }

    private fun observeConvertCurrencyLiveData() {
        currencyViewModel.convertCurrenciesLiveData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    currencyConverterAdapter.submitList(it.data)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}