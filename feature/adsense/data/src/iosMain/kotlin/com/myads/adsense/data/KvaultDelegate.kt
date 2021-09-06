package com.myads.adsense.data

import com.liftric.kvault.KVault
import platform.Foundation.NSBundle

object KvaultDelegate {
    fun getKvault(): KVault {
        return KVault(NSBundle.mainBundle.bundleIdentifier, null)
    }
}
