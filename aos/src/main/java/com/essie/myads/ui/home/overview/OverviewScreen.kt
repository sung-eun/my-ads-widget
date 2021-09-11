package com.essie.myads.ui.home.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.essie.myads.R
import com.essie.myads.common.ui.theme.AppTheme
import com.essie.myads.domain.entity.DashboardData

private val DefaultPadding = 12.dp

@Composable
fun OverviewBody(dashboardData: DashboardData) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {
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