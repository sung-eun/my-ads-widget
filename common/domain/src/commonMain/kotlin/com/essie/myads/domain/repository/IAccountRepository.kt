package com.essie.myads.domain.repository

import com.essie.myads.domain.entity.AdAccount

interface IAccountRepository {
    suspend fun getAccounts(): List<AdAccount>
    suspend fun connectAccount(account: AdAccount)
}