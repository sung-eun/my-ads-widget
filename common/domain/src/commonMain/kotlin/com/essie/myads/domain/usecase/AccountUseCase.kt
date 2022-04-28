package com.essie.myads.domain.usecase

import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.AdSupplier
import com.essie.myads.domain.repository.IAccountRepository

class AccountUseCase(private val accountRepository: IAccountRepository) {

    suspend fun removeTokenInfo() {
        accountRepository.selectAccount(AdAccount("", "", AdSupplier.ADSENSE))
    }

    suspend fun getAccounts(): List<AdAccount> {
        return accountRepository.getAccounts()
    }

    suspend fun selectAccount(account: AdAccount) {
        accountRepository.selectAccount(account)
    }

    suspend fun getSelectedAccountId(): String {
        val cachedAccountId = accountRepository.getSelectAccountId()
        if (cachedAccountId.isEmpty()) {
            getAccounts().firstOrNull()?.let {
                selectAccount(it)
                return it.id
            }
        }
        return cachedAccountId
    }
}