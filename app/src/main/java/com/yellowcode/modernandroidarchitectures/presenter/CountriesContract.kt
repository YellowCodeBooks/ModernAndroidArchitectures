package com.yellowcode.modernandroidarchitectures.presenter

import com.yellowcode.modernandroidarchitectures.model.CountryModel

class CountriesContract {
    interface PresenterInterface {
        fun onFetchCountries()
        fun getCountryInfo(countryModel: CountryModel)
    }

    interface ViewInterface {
        fun onSuccessful(result: List<CountryModel>)
        fun onError()
        fun showMessage(message: String)
    }
}