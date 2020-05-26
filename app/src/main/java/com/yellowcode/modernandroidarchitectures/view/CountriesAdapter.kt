package com.yellowcode.modernandroidarchitectures.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yellowcode.modernandroidarchitectures.R
import com.yellowcode.modernandroidarchitectures.model.CountryModel
import kotlinx.android.synthetic.main.item_country.view.*

class CountriesAdapter(val countries: ArrayList<CountryModel>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    var listener: OnItemClickListener? = null

    fun updateCountries(newCountries: List<CountryModel>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position], listener)
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val countryName = view.tvCountry
        private val countryCapital = view.tvCapital

        fun bind(country: CountryModel, listener: OnItemClickListener?) {
            countryName.text = country.name
            countryCapital.text = country.capital
            itemView.setOnClickListener { listener?.onItemClick(country) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(country: CountryModel)
    }
}