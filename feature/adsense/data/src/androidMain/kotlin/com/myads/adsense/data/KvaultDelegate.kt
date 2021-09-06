package com.myads.adsense.data

import android.content.Context
import com.liftric.kvault.KVault

object KvaultDelegate {
    fun getKvault(context: Context): KVault {
        return KVault(context)
    }
}