package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.AdSupplier
import com.essie.myads.domain.repository.IAccountRepository
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource

class AccountRepository(
    private val dataSource: AdSenseRemoteDataSource
) : IAccountRepository {

    override suspend fun getAccounts(): List<AdAccount> {
        return dataSource.getAccounts().accounts.map {
            AdAccount(
                it.name ?: "",
                it.displayName ?: "",
                AdSupplier.ADSENSE
            )
        }
    }

    override suspend fun connectAccount(account: AdAccount) {

    }
}