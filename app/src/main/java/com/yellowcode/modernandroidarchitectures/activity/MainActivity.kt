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
import com.yellowcode.modernandroidarchitectures.R
import com.yellowcode.modernandroidarchitectures.adapter.CountriesAdapter
import com.yellowcode.modernandroidarchitectures.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var apiService: CountriesApi? = null
    private val countriesAdapter = CountriesAdapter(arrayListOf())
    private var countries: List<Country>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = CountriesService.create()

        listView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

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

        apiService?.let {
            it.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    listView.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                    searchField.isEnabled = true

                    countries = result
                    countriesAdapter.updateCountries(result)
                }, { error ->
                    onError()
                })
        }
    }

    fun onError() {
        listView.visibility = View.GONE
        progress.visibility = View.GONE
        searchField.isEnabled = false

        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}
