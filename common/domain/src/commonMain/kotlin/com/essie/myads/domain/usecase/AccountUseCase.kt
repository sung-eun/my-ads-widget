package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.repository.IAccountRepository
import com.essie.myads.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun getAccounts(): List<AdAccount> {
        return accountRepository.getAccounts()
    }

    suspend fun selectAccount(account: AdAccount) {
        accountRepository.selectAccount(account)
    }

    suspend fun getSelectedAccountName(): String? {
        val cachedAccountName = accountRepository.getSelectAccountName()
        if (cachedAccountName.isEmpty()) {
            return getAccounts().firstOrNull()?.id
        }
        return cachedAccountName
    }
}