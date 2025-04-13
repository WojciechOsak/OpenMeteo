package com.wojciechosak.openmeteo.domain

import com.wojciechosak.openmeteo.network.ResultWrapper

data class LocationDomain(
    val name: String,
    val lat: Double,
    val lon: Double
)

interface ILocationRepository {
    suspend fun searchLocation(name: String): ResultWrapper<List<LocationDomain>>
}