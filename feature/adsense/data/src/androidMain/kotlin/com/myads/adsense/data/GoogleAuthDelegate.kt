package com.myads.adsense.data

import android.content.Context
import com.myads.adsense.data.datasource.remote.IHttpHeaderProvider

class GoogleAuthDelegate(
    private val context: Context,
    private val accessTokenProvider: IGoogleAccessTokenProvider
) : IHttpHeaderProvider {

    override fun getAccessToken(): String {
        return accessTokenProvider.getAccessToken(context)
    }
}

interface IGoogleAccessTokenProvider {
    fun getAccessToken(context: Context): String
}