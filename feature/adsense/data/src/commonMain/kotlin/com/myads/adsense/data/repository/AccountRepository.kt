package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.AdSupplier
import com.essie.myads.domain.repository.IAccountRepository
import com.myads.adsense.data.datasource.local.AdSenseLocalDataSource
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource

class AccountRepository(
    private val remoteDataSource: AdSenseRemoteDataSource,
    private val localDataSource: AdSenseLocalDataSource
) : IAccountRepository {

    override suspend fun getAccounts(): List<AdAccount> {
        return remoteDataSource.getAccounts().accounts.map {
            AdAccount(
                it.name ?: "",
                it.displayName ?: "",
                AdSupplier.ADSENSE
            )
        }
    }

    override suspend fun selectAccount(account: AdAccount) {
        localDataSource.putAccountName(account.id)
    }

    override suspend fun getSelectAccountId(): String {
        return localDataSource.getAccountName()
    }
}