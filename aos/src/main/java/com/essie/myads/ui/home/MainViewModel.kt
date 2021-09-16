package com.essie.myads.ui.home

import androidx.lifecycle.*
import com.essie.myads.domain.entity.AdAccount
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.usecase.AccountUseCase
import com.essie.myads.domain.usecase.DashboardDataUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val accountUseCase: AccountUseCase,
    private val dashboardDataUseCase: DashboardDataUseCase,
    private val googleAccountManager: IGoogleAccountManager
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    val refreshing: Flow<Boolean> = _refreshing

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<HomeErrorType?> = MutableLiveData(null)
    val error: LiveData<HomeErrorType?> = _error

    private val _dashboardData = MutableLiveData(DashboardData())
    val dashboardData: LiveData<DashboardData> = _dashboardData

    private val _googleAccount: MutableLiveData<GoogleSignInAccount?> = MutableLiveData(null)
    val googleAccount: LiveData<GoogleSignInAccount?> = _googleAccount

    private val _selectedAdAccountName: MutableLiveData<String> = MutableLiveData("")
    val selectedAdAccountName: LiveData<String> = _selectedAdAccountName

    private val _adAccounts: MutableLiveData<List<AdAccount>> = MutableLiveData(emptyList())
    val adAccounts: LiveData<List<AdAccount>> = _adAccounts

    @FlowPreview
    fun pullToRefresh() {
        viewModelScope.launch {
            _refreshing.emit(true)
            refresh()
            _refreshing.emit(false)
        }
    }

    @FlowPreview
    suspend fun fetchInitData(authCode: String?) {
        if (_googleAccount.value == null) {
            _error.postValue(HomeErrorType.ACCOUNT_NOT_CONNECTED)
        } else if (googleAccountManager.hasAdSensePermission().not()) {
            _error.postValue(HomeErrorType.ADSENSE_NOT_PERMITTED)
        }

        if (authCode.isNullOrEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.fetchAndSaveAuthToken(authCode)
            refresh()
            fetchAdAccounts()
        }
    }

    fun refreshToken() {
        viewModelScope.launch(Dispatchers.IO) {
            accountUseCase.refreshToken()
        }
    }

    @FlowPreview
    fun refresh() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(accountUseCase.getSelectedAccountId())
                .flatMapConcat {
                    if (it.isEmpty()) {
                        if (_googleAccount.value == null) {
                            _error.postValue(HomeErrorType.ACCOUNT_NOT_CONNECTED)
                        } else if (googleAccountManager.hasAdSensePermission().not()) {
                            _error.postValue(HomeErrorType.ADSENSE_NOT_PERMITTED)
                        }
                        return@flatMapConcat emptyFlow()
                    }
                    _error.postValue(null)
                    return@flatMapConcat flowOf(
                        dashboardDataUseCase.getDashboardData(
                            it,
                            DateRange.LAST_7DAYS
                        )
                    )
                }
                .onCompletion { _loading.postValue(false) }
                .collect { _dashboardData.postValue(it) }
        }
    }

    fun updateGoogleAccount(account: GoogleSignInAccount?) {
        _googleAccount.value = account
        if (account == null) {
            _dashboardData.value = DashboardData()
            _error.value = HomeErrorType.ACCOUNT_NOT_CONNECTED
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (account == null) {
                accountUseCase.removeTokenInfo()
            }
        }
    }

    private fun fetchAdAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(accountUseCase.getSelectedAccountId())
                .zip(flowOf(accountUseCase.getAccounts())) { selectedAccountName, accounts ->
                    if (selectedAccountName.isEmpty() && accounts.isNotEmpty()) {
                        _selectedAdAccountName.postValue(accounts[0].id)
                    } else {
                        _selectedAdAccountName.postValue(selectedAccountName)
                    }
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
                .collect { _selectedAdAccountName.postValue(adAccount.id) }
        }
    }
}

class MainViewModelFactory(
    private val accountUseCase: AccountUseCase,
    private val dashboardDataUseCase: DashboardDataUseCase,
    private val googleAccountManager: IGoogleAccountManager
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(accountUseCase, dashboardDataUseCase, googleAccountManager) as T
    }
}