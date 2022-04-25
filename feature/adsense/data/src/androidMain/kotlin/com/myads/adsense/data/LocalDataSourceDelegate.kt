package com.myads.adsense.data

import android.content.Context
import com.liftric.kvault.KVault
import com.myads.adsense.data.datasource.local.AdSenseLocalDataSource

object LocalDataSourceDelegate {
    fun getAdSenseLocalDataSource(context: Context): AdSenseLocalDataSource {
        return AdSenseLocalDataSource(KVault(context))
    }
}