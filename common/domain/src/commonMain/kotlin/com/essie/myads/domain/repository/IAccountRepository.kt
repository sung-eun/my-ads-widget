package com.essie.myads.domain.repository

import com.essie.myads.domain.entity.Account

interface IAccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun connectAccount(account: Account)
}