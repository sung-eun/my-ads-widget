package com.myads.adsense.data.repository

import com.essie.myads.domain.entity.Account
import com.essie.myads.domain.entity.AdSupplier
import com.essie.myads.domain.repository.IAccountRepository
import com.myads.adsense.data.datasource.AdSenseRemoteDataSource

class AccountRepository(private val dataSource: AdSenseRemoteDataSource) : IAccountRepository {
    override suspend fun getAccounts(): List<Account> {
        return dataSource.getAccounts().accounts?.map {
            Account(
                it.name ?: "",
                it.displayName ?: "",
                AdSupplier.ADSENSE
            )
        } ?: emptyList()
    }

    override suspend fun connectAccount(account: Account) {

    }
}