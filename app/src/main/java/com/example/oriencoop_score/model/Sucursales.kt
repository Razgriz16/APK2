package com.example.oriencoop_score.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


data class Region(
    val name: String,
    val cities: List<City> = emptyList(),
    val isExpanded: MutableState<Boolean> = mutableStateOf(false)
)

data class City(
    val name: String,
    val branches: List<Branch> = emptyList(),
    val isExpanded: MutableState<Boolean> = mutableStateOf(false)
)
data class Branch(
    val name: String,
    val address: String,
    val phone: String,
    val agent: String,
    val schedule: List<String>,
    val latitude: Double,
    val longitude: Double,
    var isExpanded: Boolean = false
)
