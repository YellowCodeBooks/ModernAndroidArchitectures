package com.yellowcode.modernandroidarchitectures.model

data class CountryModel(val name: String, val capital: String) {

    fun getCountryInfo() = "Country $name, capital is $capital clicked"
}