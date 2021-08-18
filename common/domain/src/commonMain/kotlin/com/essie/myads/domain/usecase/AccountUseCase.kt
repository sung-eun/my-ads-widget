package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.Account
import com.essie.myads.domain.repository.IAccountRepository

class AccountUseCase(private val repository: IAccountRepository) {
    suspend fun getAccounts(): List<Account> {
        return repository.getAccounts()
    }

    suspend fun connectAccount(account: Account) {
        repository.connectAccount(account)
    }
}