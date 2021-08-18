package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.repository.IAdsRepository

class DashboardDataUseCase(private val repository: IAdsRepository) {
    suspend fun getDashboardData(dateRange: DateRange = DateRange.LAST_7DAYS): DashboardData {
        return repository.getAdsData(dateRange)
    }
}