package com.essie.myads.domain.repository

import com.essie.myads.domain.entity.AdAccount

interface IAccountRepository {
    suspend fun getAccounts(): List<AdAccount>
    suspend fun selectAccount(account: AdAccount)
    suspend fun getSelectAccountName(): String
}