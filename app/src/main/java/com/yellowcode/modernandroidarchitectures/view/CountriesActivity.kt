package com.yellowcode.modernandroidarchitectures.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yellowcode.modernandroidarchitectures.presenter.CountriesPresenter
import com.yellowcode.modernandroidarchitectures.databinding.ActivityCountriesBinding
import com.yellowcode.modernandroidarchitectures.model.CountryModel
import com.yellowcode.modernandroidarchitectures.networking.CountriesApi
import com.yellowcode.modernandroidarchitectures.networking.CountriesService
import com.yellowcode.modernandroidarchitectures.presenter.CountriesContract

class CountriesActivity : AppCompatActivity(), CountriesContract.ViewInterface {

    private lateinit var binding: ActivityCountriesBinding
    private lateinit var apiService: CountriesApi
    private lateinit var countriesPresenter: CountriesPresenter
    private val countriesAdapter = CountriesAdapter(arrayListOf())
    private var countries: List<CountryModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiService = CountriesService.create()
        countriesPresenter = CountriesPresenter(this, apiService)

        binding.listView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }
        countriesAdapter.setOnItemClickListener(object : CountriesAdapter.OnItemClickListener {
            override fun onItemClick(country: CountryModel) {
                countriesPresenter.getCountryInfo(country)
            }
        })

        binding.searchField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    val filterCountries = countries.filter { country ->
                        country.name.common.contains(s.toString(), true)
                    }
                    filterCountries.let { countriesAdapter.updateCountries(it) }
                } else {
                    countries.let { countriesAdapter.updateCountries(it) }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        onFetchCountries()
    }

    private fun onFetchCountries() {
        binding.listView.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        binding.searchField.isEnabled = false

        countriesPresenter.onFetchCountries()
    }

    override fun onSuccessful(result: List<CountryModel>) {
        binding.listView.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
        binding.searchField.isEnabled = true

        countries = result
        countriesAdapter.updateCountries(countries)
    }

    override fun onError() {
        binding.listView.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.searchField.isEnabled = false

        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this@CountriesActivity, message, Toast.LENGTH_SHORT).show()
    }
}
