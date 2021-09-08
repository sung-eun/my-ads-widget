package com.myads.adsense.data.datasource.remote

import com.essie.myads.domain.entity.DateRange
import com.myads.adsense.data.model.response.ResponseAccountList
import com.myads.adsense.data.model.response.ResponsePayment
import com.myads.adsense.data.model.response.ResponsePayments
import com.myads.adsense.data.model.response.ResponseReport
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

private const val BASE_URL = "https://adsense.googleapis.com/v2"

class AdSenseRemoteDataSource(
    private val headerProvider: IHttpHeaderProvider,
    private val debuggable: Boolean
) {

    private val httpClient = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = if (debuggable) LogLevel.ALL else LogLevel.NONE
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
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
            parametersOf(
                "dateRange" to listOf(if (dateRange == DateRange.LAST_30DAYS) "LAST_30_DAYS" else "LAST_7_DAYS"),
                "metrics" to listOf("IMPRESSIONS", "CLICKS", "ESTIMATED_EARNINGS")
            )
        }
    }

    suspend fun getBalance(accountId: String): ResponsePayment? {
        val payments = httpClient.get<ResponsePayments> {
            url("$BASE_URL/$accountId/payments")
            header("Authorization", "Bearer ${headerProvider.getAccessToken()}")
        }
        return payments.payments?.firstOrNull { it.name == "$accountId/payments/unpaid" }
    }
}