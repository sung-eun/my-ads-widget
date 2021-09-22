package com.myads.adsense.data

import com.liftric.kvault.KVault
import com.myads.adsense.data.datasource.local.AdSenseLocalDataSource
import com.myads.adsense.data.datasource.local.AuthLocalDataSource
import platform.Foundation.NSBundle

object LocalDataSourceDelegate {
    fun getAdSenseLocalDataSource(): AdSenseLocalDataSource {
        return AdSenseLocalDataSource(KVault(NSBundle.mainBundle.bundleIdentifier, null))
    }

    fun getAuthLocalDataSource(): AuthLocalDataSource {
        return AuthLocalDataSource(KVault(NSBundle.mainBundle.bundleIdentifier, null))
    }
}