package com.myads.adsense.data.datasource.remote

import com.myads.adsense.data.model.response.ResponseAccessToken
import com.myads.adsense.data.model.response.ResponseRefreshToken
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class GoogleAuthRemoteDataSource(private val debuggable: Boolean) {
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

        expectSuccess = false
    }

    suspend fun getAccessToken(authCode: String): ResponseAccessToken {
        val parameters = Parameters.build {
            append("code", authCode)
            append("client_id", ServerConfig.CLIENT_ID)
            append("client_secret", ServerConfig.CLIENT_SECRET)
            append("redirect_uri", "urn:ietf:wg:oauth:2.0:oob:auto")
            append("grant_type", "authorization_code")
        }

        return httpClient.post {
            url("https://oauth2.googleapis.com/token")
            body = FormDataContent(parameters)
        }
    }

    suspend fun refreshToken(refreshToken: String): ResponseRefreshToken {
        val parameters = Parameters.build {
            append("refreshToken", refreshToken)
            append("client_id", ServerConfig.CLIENT_ID)
            append("client_secret", ServerConfig.CLIENT_SECRET)
            append("grant_type", "refresh_token")
        }

        return httpClient.post {
            url("https://oauth2.googleapis.com/token")
            body = FormDataContent(parameters)
        }
    }
}