package com.essie.myads.ui.home.overview

import android.util.Log
import androidx.lifecycle.*
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.domain.usecase.AccountUseCase
import com.essie.myads.domain.usecase.DashboardDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val accountUseCase: AccountUseCase,
    private val dashboardDataUseCase: DashboardDataUseCase
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    val refreshing: Flow<Boolean> = _refreshing

    private val _hasAccount = MutableLiveData(true)
    val hasAccount: LiveData<Boolean> = _hasAccount

    private val _dashboardData = MutableLiveData(DashboardData())
    val dashboardData: LiveData<DashboardData> = _dashboardData

    @FlowPreview
    fun pullToRefresh() {
        viewModelScope.launch {
            _refreshing.emit(true)
            refresh()
            _refreshing.emit(false)
        }
    }

    @FlowPreview
    fun fetchInitData(authCode: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (authCode.isNullOrEmpty()) {
                accountUseCase.removeTokenInfo()
                _hasAccount.postValue(false)
            } else {
                flowOf(accountUseCase.fetchAndSaveAuthToken(authCode))
                    .collect { refresh() }
            }
        }
    }

    @FlowPreview
    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            flowOf(accountUseCase.refreshToken())
                .flatMapConcat { flowOf(accountUseCase.getSelectedAccountName()) }
                .flatMapConcat {
                    if (it.isNullOrEmpty()) {
                        _hasAccount.postValue(false)
                        return@flatMapConcat emptyFlow()
                    }
                    _hasAccount.postValue(true)
                    return@flatMapConcat flowOf(
                        dashboardDataUseCase.getDashboardData(
                            it,
                            DateRange.LAST_7DAYS
                        )
                    )
                }
                .catch { Log.d("TAG", "${it.message}") }
                .collect { _dashboardData.postValue(it) }
        }
    }
}

class OverviewViewModelFactory(
    private val accountUseCase: AccountUseCase,
    private val dashboardDataUseCase: DashboardDataUseCase
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OverviewViewModel(accountUseCase, dashboardDataUseCase) as T
    }
}