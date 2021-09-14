package com.essie.myads.ui.home.settings

import androidx.lifecycle.*
import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.usecase.AccountUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class SettingsViewModel(private val accountUseCase: AccountUseCase) : ViewModel() {

    private val _googleAccount: MutableLiveData<GoogleSignInAccount?> = MutableLiveData(null)
    val googleAccount: LiveData<GoogleSignInAccount?> = _googleAccount

    private val _selectedAdAccountName: MutableLiveData<String> = MutableLiveData("")
    val selectedAdAccountName: LiveData<String> = _selectedAdAccountName

    private val _adAccounts: MutableLiveData<List<AdAccount>> = MutableLiveData(emptyList())
    val adAccounts: LiveData<List<AdAccount>> = _adAccounts

    fun updateGoogleAccount(account: GoogleSignInAccount?) {
        _googleAccount.value = account
    }

    fun fetchAdAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(accountUseCase.getSelectedAccountName())
                .zip(flowOf(accountUseCase.getAccounts())) { selectedAccountName, accounts ->
                    _selectedAdAccountName.postValue(selectedAccountName ?: "")
                    _adAccounts.postValue(accounts)
                }
                .catch { }
                .collect()
        }
    }

    fun selectAdAccount(adAccount: AdAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(accountUseCase.selectAccount(adAccount))
                .catch { }
                .collect { _selectedAdAccountName.postValue(adAccount.displayName) }
        }
    }
}

class SettingsViewModelFactory(private val accountUseCase: AccountUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(accountUseCase) as T
    }
}