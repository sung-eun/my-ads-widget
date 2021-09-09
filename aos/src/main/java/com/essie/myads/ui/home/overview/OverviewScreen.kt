package com.essie.myads.ui.home.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

private val DefaultPadding = 12.dp

@Composable
fun OverviewBody() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {
        OverviewCard(
            title = "Unpaid",
            amountText = ""
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = "Estimated Income (Last 7days)",
            amountText = ""
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = "Clicks",
            amountText = ""
        )
        Spacer(Modifier.height(DefaultPadding))
        OverviewCard(
            title = "Impressions",
            amountText = ""
        )
        Spacer(Modifier.height(DefaultPadding))
    }
}

@Composable
private fun OverviewCard(
    title: String,
    amountText: String
) {
    Card {
        Column {
            Column(Modifier.padding(DefaultPadding)) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
                Text(text = amountText, style = MaterialTheme.typography.h2)
            }
        }
    }
}