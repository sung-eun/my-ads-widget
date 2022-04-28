package com.myads.adsense.data.datasource.remote

import com.essie.myads.domain.entity.DateRange
import com.myads.adsense.data.HttpClientEngineProvider
import com.myads.adsense.data.model.response.ResponseAccountList
import com.myads.adsense.data.model.response.ResponsePayment
import com.myads.adsense.data.model.response.ResponsePayments
import com.myads.adsense.data.model.response.ResponseReport
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

private const val BASE_URL = "https://adsense.googleapis.com/v2"

class AdSenseRemoteDataSource(private val headerProvider: IHttpHeaderProvider) {

    private val httpClient = HttpClient(HttpClientEngineProvider.httpClientEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        expectSuccess = false
    }

    suspend fun getAccounts(): ResponseAccountList {
        return httpClient.get {
            url("$BASE_URL/accounts")
            header("Authorization", "Bearer ${headerProvider.getAccessToken()}")
        }
    }

    suspend fun getReports(accountId: String, dateRange: DateRange): ResponseReport {
        return httpClient.get {
            url("$BASE_URL/$accountId/reports:generate")
            header("Authorization", "Bearer ${headerProvider.getAccessToken()}")
            parameter(
                "dateRange",
                if (dateRange == DateRange.LAST_30DAYS) "LAST_30_DAYS" else "LAST_7_DAYS"
            )
            parameter("metrics", "IMPRESSIONS")
            parameter("metrics", "CLICKS")
            parameter("metrics", "ESTIMATED_EARNINGS")
        }
    }

    suspend fun getUnpaidAmount(accountId: String): ResponsePayment? {
        val payments = httpClient.get<ResponsePayments> {
            url("$BASE_URL/$accountId/payments")
            header("Authorization", "Bearer ${headerProvider.getAccessToken()}")
        }
        return payments.payments?.firstOrNull { it.name == "$accountId/payments/unpaid" }
    }
}