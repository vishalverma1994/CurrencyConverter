package com.currencyconverter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencyconverter.databinding.AdapterCurrencyConverterBinding
import com.currencyconverter.model.ConvertedRates
import com.currencyconverter.model.CurrencyConverterResponse

class CurrencyConverterAdapter : ListAdapter<ConvertedRates, CurrencyConverterAdapter.ViewHolder>(MyDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterCurrencyConverterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(getItem(position))
    }

    inner class ViewHolder(private val binding: AdapterCurrencyConverterBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindViews(item: ConvertedRates) {
            binding.apply {
                tvCurrency.text = item.currencySymbol
                tvCurrencyAmount.text = item.currencyRates.toString()
            }
        }

    }
}

class MyDiffUtils : DiffUtil.ItemCallback<ConvertedRates>() {
    override fun areItemsTheSame(oldItem: ConvertedRates, newItem: ConvertedRates): Boolean {
        return oldItem.currencySymbol == newItem.currencySymbol
    }

    override fun areContentsTheSame(oldItem: ConvertedRates, newItem: ConvertedRates): Boolean {
        return oldItem == newItem
    }

}