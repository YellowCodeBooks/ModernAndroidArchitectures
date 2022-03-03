package com.yellowcode.modernandroidarchitectures.presenter

import com.yellowcode.modernandroidarchitectures.model.CountryModel
import com.yellowcode.modernandroidarchitectures.networking.CountriesApi
import com.yellowcode.modernandroidarchitectures.view.CountriesActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CountriesPresenter(
    private val viewInterface: CountriesContract.ViewInterface,
    private val apiService: CountriesApi
) : CountriesContract.PresenterInterface {
    override fun onFetchCountries() {
        apiService.let {
            it.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    viewInterface.onSuccessful(result)
                }, { error ->
                    viewInterface.onError()
                })
        }
    }

    override fun getCountryInfo(country: CountryModel) {
        val countryInfo = country.getCountryInfo()
        viewInterface.showMessage(countryInfo)
    }
}