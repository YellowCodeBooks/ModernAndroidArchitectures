package com.yellowcode.modernandroidarchitectures.model

data class Name(val common: String)

data class Country(val name: Name, val capital: List<String>?)