package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.Account
import com.essie.myads.domain.repository.IAccountRepository
import com.essie.myads.domain.repository.IAuthRepository

class AccountUseCase(
    private val authRepository: IAuthRepository,
    private val accountRepository: IAccountRepository
) {
    suspend fun fetchAndSaveAuthToken(authCode: String) {
        authRepository.fetchAndSaveAuthToken(authCode)
    }

    suspend fun refreshToken() {
        authRepository.refreshToken()
    }

    suspend fun removeTokenInfo() {
        authRepository.removeAuthToken()
    }

    suspend fun getAccounts(): List<Account> {
        return accountRepository.getAccounts()
    }

    suspend fun connectAccount(account: Account) {
        accountRepository.connectAccount(account)
    }
}