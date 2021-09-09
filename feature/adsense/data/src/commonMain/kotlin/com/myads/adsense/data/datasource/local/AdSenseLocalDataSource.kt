package com.myads.adsense.data.datasource.local

import com.liftric.kvault.KVault

private const val KEY_ACCOUNT_NAME = "KEY_ACCOUNT_NAME"

class AdSenseLocalDataSource(private val kVault: KVault) {

    fun putAccountName(accountName: String?) {
        if (accountName.isNullOrEmpty()) {
            kVault.deleteObject(KEY_ACCOUNT_NAME)
        } else {
            kVault.set(KEY_ACCOUNT_NAME, accountName)
        }
    }

    fun getAccountName(): String {
        return kVault.string(KEY_ACCOUNT_NAME) ?: ""
    }
}