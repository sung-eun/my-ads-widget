package com.myads.adsense.data.datasource.local

import com.liftric.kvault.KVault
import com.myads.adsense.data.datasource.remote.IHttpHeaderProvider

private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"

class AuthLocalDataSource(private val kVault: KVault) : IHttpHeaderProvider {

    fun putAccessToken(accessToken: String?) {
        if (accessToken.isNullOrEmpty()) {
            kVault.deleteObject(KEY_ACCESS_TOKEN)
        } else {
            kVault.set(KEY_ACCESS_TOKEN, accessToken)
        }
    }

    override fun getAccessToken(): String {
        return kVault.string(KEY_ACCESS_TOKEN) ?: ""
    }

    fun putRefreshToken(refreshToken: String?) {
        if (refreshToken.isNullOrEmpty()) {
            kVault.deleteObject(KEY_REFRESH_TOKEN)
        } else {
            kVault.set(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getRefreshToken(): String {
        return kVault.string(KEY_REFRESH_TOKEN) ?: ""
    }
}