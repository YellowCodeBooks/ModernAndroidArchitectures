package com.yellowcode.modernandroidarchitectures.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yellowcode.modernandroidarchitectures.R
import com.yellowcode.modernandroidarchitectures.contoller.CountriesController
import com.yellowcode.modernandroidarchitectures.model.CountryModel
import kotlinx.android.synthetic.main.activity_main.*

class CountriesActivity : AppCompatActivity() {

    lateinit var countriesController: CountriesController
    private val countriesAdapter = CountriesAdapter(arrayListOf())
    private var countries: List<CountryModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countriesController = CountriesController(this)

        listView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }
        countriesAdapter.setOnItemClickListener(object : CountriesAdapter.OnItemClickListener {
            override fun onItemClick(country: CountryModel) {
                Toast.makeText(this@CountriesActivity, country.getCountryInfo(), Toast.LENGTH_SHORT).show()
            }
        })

        searchField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    val filterCountries = countries?.filter { country ->
                        country.name.contains(s.toString(), true)
                    }
                    filterCountries?.let { countriesAdapter.updateCountries(it) }
                } else {
                    countries?.let { countriesAdapter.updateCountries(it) }
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

    fun onFetchCountries() {
        listView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        searchField.isEnabled = false

        countriesController.onFetchCountries()
    }

    fun onSuccessful(result: List<CountryModel>) {
        listView.visibility = View.VISIBLE
        progress.visibility = View.GONE
        searchField.isEnabled = true

        countries = result
        countriesAdapter.updateCountries(countries)
    }

    fun onError() {
        listView.visibility = View.GONE
        progress.visibility = View.GONE
        searchField.isEnabled = false

        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}
