package com.yellowcode.modernandroidarchitectures.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yellowcode.modernandroidarchitecruesdemo.networking.CountriesApi
import com.yellowcode.modernandroidarchitecruesdemo.networking.CountriesService
import com.yellowcode.modernandroidarchitectures.adapter.CountriesAdapter
import com.yellowcode.modernandroidarchitectures.databinding.ActivityMainBinding
import com.yellowcode.modernandroidarchitectures.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var apiService: CountriesApi? = null
    private val countriesAdapter = CountriesAdapter(arrayListOf())
    private var countries: List<Country>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiService = CountriesService.create()

        binding.listView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }
        countriesAdapter.setOnItemClickListener(object : CountriesAdapter.OnItemClickListener {
            override fun onItemClick(country: Country) {
                Toast.makeText(this@MainActivity, "Country ${country.name}, capital is ${country.capital} clicked", Toast.LENGTH_SHORT).show()
            }
        })

        binding.searchField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    val filterCountries = countries?.filter { country ->
                        country.name.common.contains(s.toString(), true)
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
        binding.listView.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        binding.searchField.isEnabled = false

        apiService?.let {
            it.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    binding.listView.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.searchField.isEnabled = true

                    countries = result
                    countriesAdapter.updateCountries(result)
                }, { error ->
                    onError()
                })
        }
    }

    fun onError() {
        binding.listView.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.searchField.isEnabled = false

        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}
