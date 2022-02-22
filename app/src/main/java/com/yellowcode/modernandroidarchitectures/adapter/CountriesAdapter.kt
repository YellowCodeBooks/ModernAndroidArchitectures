package com.yellowcode.modernandroidarchitectures.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yellowcode.modernandroidarchitectures.databinding.ItemCountryBinding
import com.yellowcode.modernandroidarchitectures.model.Country

class CountriesAdapter(val countries: ArrayList<Country>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    var listener: OnItemClickListener? = null

    fun updateCountries(newCountries: List<Country>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        ItemCountryBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position], listener)
    }

    class CountryViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country, listener: OnItemClickListener?) {
            binding.apply {
                tvCountry.text = country.name.common
                tvCapital.text = country.capital?.joinToString(", ")
                root.setOnClickListener { listener?.onItemClick(country) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(country: Country)
    }
}