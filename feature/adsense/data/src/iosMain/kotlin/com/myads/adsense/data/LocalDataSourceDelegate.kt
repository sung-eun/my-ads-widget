package com.myads.adsense.data

import android.content.Context
import com.liftric.kvault.KVault
import com.myads.adsense.data.datasource.local.AdSenseLocalDataSource
import com.myads.adsense.data.datasource.local.AuthLocalDataSource
import platform.Foundation.NSBundle

object LocalDataSourceDelegate {
    fun getAdSenseLocalDataSource(context: Context): AdSenseLocalDataSource {
        return AdSenseLocalDataSource(KVault(NSBundle.mainBundle.bundleIdentifier, null))
    }

    fun getAuthLocalDataSource(context: Context): AuthLocalDataSource {
        return AuthLocalDataSource(KVault(NSBundle.mainBundle.bundleIdentifier, null))
    }
}