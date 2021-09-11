package com.essie.myads.ui.home.overview

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
    fun refresh() {
        viewModelScope.launch {
            flowOf(accountUseCase.getSelectedAccountName())
                .map {
                    if (it.isNullOrEmpty()) {
                        return@map DashboardData()
                    }
                    return@map dashboardDataUseCase.getDashboardData(
                        it,
                        DateRange.LAST_7DAYS
                    )
                }
                .flowOn(Dispatchers.IO)
                .collect {_dashboardData.postValue(it)}
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