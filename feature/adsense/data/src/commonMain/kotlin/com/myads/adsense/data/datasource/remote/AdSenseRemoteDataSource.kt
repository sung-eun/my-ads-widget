package com.myads.adsense.data.datasource.remote

import com.myads.adsense.data.model.response.ResponseAccountList
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

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
}