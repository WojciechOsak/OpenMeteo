package com.wojciechosak.openmeteo.data

import com.wojciechosak.openmeteo.domain.ILocationRepository
import com.wojciechosak.openmeteo.domain.LocationDomain
import com.wojciechosak.openmeteo.network.ResultWrapper

class LocationRepository: ILocationRepository {
    override suspend fun searchLocation(name: String): ResultWrapper<List<LocationDomain>> {
        return ResultWrapper.Success(
            listOf(
                LocationDomain("Warsaw", 52.1413,13.1152),
                LocationDomain("Berlin", 52.3112,13.2418),
                LocationDomain("Madrid", 40.2503,3.4213),
                LocationDomain("Kabompo", -13.5211,24.2265),
            )
        )
    }
}