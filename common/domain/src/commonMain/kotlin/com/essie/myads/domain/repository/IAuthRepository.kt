package com.essie.myads.domain.repository

interface IAuthRepository {
    suspend fun fetchAndSaveAuthToken(authCode: String)
    suspend fun refreshToken()
    suspend fun removeAuthToken()
}