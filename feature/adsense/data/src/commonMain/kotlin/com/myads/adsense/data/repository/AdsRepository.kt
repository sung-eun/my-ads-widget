package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.repository.IAdsRepository
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource

class AdsRepository(private val dataSource: AdSenseRemoteDataSource) : IAdsRepository {
    override suspend fun getAdsData(dateRange: DateRange): DashboardData {
        TODO()
    }
}