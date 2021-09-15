package com.essie.myads.ui.home.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.essie.myads.R
import com.essie.myads.common.ui.theme.AppTheme
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.ui.home.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview

private val DefaultPadding = 12.dp

@FlowPreview
@Composable
fun OverviewBody(mainViewModel: MainViewModel) {
    val refreshing by mainViewModel.refreshing.collectAsState(false)
    val loading by mainViewModel.loading.observeAsState(initial = false)
    val dashboardData by mainViewModel.dashboardData.observeAsState()
    val hasAccount by mainViewModel.hasAccount.observeAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = { mainViewModel.pullToRefresh() }) {
        Column(Modifier.fillMaxWidth()) {
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (hasAccount != true) {
                Row(
                    modifier = Modifier
                        .background(colorResource(R.color.yellow))
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(R.string.message_no_account),
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Light
                        ),
                        color = MaterialTheme.colors.surface,
                    )
                }
            }
            OverviewContent(dashboardData ?: DashboardData())
        }
    }
}

@Composable
private fun OverviewContent(dashboardData: DashboardData) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {
        Text(
            text = stringResource(R.string.last_7_days),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.primary
        )
        OverviewCard(
            title = stringResource(R.string.balance),
            amountText = dashboardData.totalUnpaidAmount
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = stringResource(R.string.estimated_earnings),
            amountText = dashboardData.recentlyEstimatedIncome
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = stringResource(R.string.clicks),
            amountText = "${dashboardData.clicks}"
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = stringResource(R.string.impressions),
            amountText = "${dashboardData.impressions}"
        )
        Spacer(Modifier.height(DefaultPadding))
    }
}

@Composable
private fun OverviewCard(
    title: String,
    amountText: String
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(start = DefaultPadding, end = DefaultPadding)) {
            Text(text = title, style = MaterialTheme.typography.subtitle1)
            Text(
                text = amountText,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Preview
@Composable
fun PreviewOverviewCard() {
    AppTheme {
        OverviewCard("Balance", "$100")
    }
}