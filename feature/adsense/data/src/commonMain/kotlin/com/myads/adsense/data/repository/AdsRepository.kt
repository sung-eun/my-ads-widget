package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.repository.IAdsRepository
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource
import com.myads.adsense.data.model.response.ResponseReport

class AdsRepository(private val dataSource: AdSenseRemoteDataSource) : IAdsRepository {
    override suspend fun getAdsData(account: AdAccount, dateRange: DateRange): DashboardData {
        return getAdsData(account.id, dateRange)
    }

    override suspend fun getAdsData(accountName: String, dateRange: DateRange): DashboardData {
        val responseReport = dataSource.getReports(accountName, dateRange)
        val responsePayment = dataSource.getUnpaidAmount(accountName)
        return DashboardData(
            impressions = getReportValue(responseReport, "IMPRESSIONS")?.toLongOrNull() ?: 0L,
            clicks = getReportValue(responseReport, "CLICKS")?.toLongOrNull() ?: 0L,
            recentlyEstimatedIncome = getReportValue(responseReport, "ESTIMATED_EARNINGS")
                ?: "$0.00",
            dateRange = dateRange,
            totalUnpaidAmount = responsePayment?.amount ?: "0"
        )
    }

    private fun getReportValue(response: ResponseReport, type: String): String? {
        response.totalCell ?: return null
        val typeIndex = response.headers?.indexOfFirst { it.name == type } ?: -1
        return if (typeIndex < 0) {
            null
        } else {
            response.totalCell.cells?.get(typeIndex)?.value
        }
    }
}