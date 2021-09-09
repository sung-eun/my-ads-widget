package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.repository.IAdsRepository
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource
import com.myads.adsense.data.model.response.ResponseReport

class AdsRepository(private val dataSource: AdSenseRemoteDataSource) : IAdsRepository {
    override suspend fun getAdsData(account: AdAccount, dateRange: DateRange): DashboardData {
        val responseReport = dataSource.getReports(account.id, dateRange)
        val responsePayment = dataSource.getUnpaidAmount(account.id)
        return DashboardData(
            impression = getReportValue(responseReport, "IMPRESSIONS"),
            click = getReportValue(responseReport, "CLICKS"),
            recentlyEstimatedIncome = getReportValue(responseReport, "ESTIMATED_EARNINGS"),
            dateRange = dateRange,
            totalUnpaidAmount = responsePayment?.amount ?: "0"
        )
    }

    private fun getReportValue(response: ResponseReport, type: String): Long {
        response.totalCell ?: return 0L
        val typeIndex = response.headers?.indexOfFirst { it.name == type } ?: -1
        return if (typeIndex < 0) {
            0L
        } else {
            response.totalCell.cells?.get(typeIndex)?.value?.toLongOrNull() ?: 0L
        }
    }
}