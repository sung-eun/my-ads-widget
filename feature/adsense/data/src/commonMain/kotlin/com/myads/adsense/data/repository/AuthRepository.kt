package com.myads.adsense.data.repository

import com.essie.myads.domain.repository.IAuthRepository
import com.myads.adsense.data.datasource.local.AuthLocalDataSource
import com.myads.adsense.data.datasource.remote.GoogleAuthRemoteDataSource

class AuthRepository(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: GoogleAuthRemoteDataSource
) : IAuthRepository {

    override suspend fun fetchAndSaveAuthToken(authCode: String) {
        val response = remoteDataSource.getAccessToken(authCode)
        localDataSource.putAccessToken(response.accessToken ?: "")
        localDataSource.putRefreshToken(response.refreshToken ?: "")
    }

    override suspend fun refreshToken() {
        val refreshToken = localDataSource.getRefreshToken()
        if (refreshToken.isEmpty()) {
            return
        }
        val response = remoteDataSource.refreshToken(refreshToken)
        localDataSource.putAccessToken(response.accessToken)
    }

    override suspend fun removeAuthToken() {
        localDataSource.putAccessToken(null)
        localDataSource.putRefreshToken(null)
    }
}