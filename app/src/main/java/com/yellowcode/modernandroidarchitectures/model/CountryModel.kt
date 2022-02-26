package com.yellowcode.modernandroidarchitectures.model

data class CountryModel(val name: NameModel, val capital: List<String>?) {
    fun getCountryInfo() = "Country ${name.common}, capital is ${capital?.joinToString(", ")} - clicked"
}