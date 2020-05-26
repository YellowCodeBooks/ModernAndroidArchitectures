package com.yellowcode.modernandroidarchitectures.contoller

import com.yellowcode.modernandroidarchitectures.networking.CountriesApi
import com.yellowcode.modernandroidarchitectures.networking.CountriesService
import com.yellowcode.modernandroidarchitectures.view.CountriesActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CountriesController {

    private var view: CountriesActivity
    private var apiService: CountriesApi? = null

    constructor(view: CountriesActivity) {
        this.view = view
        apiService = CountriesService.create()
    }

    fun onFetchCountries() {
        apiService?.let {
            it.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.onSuccessful(result)
                }, { error ->
                    view.onError()
                })
        }
    }
}