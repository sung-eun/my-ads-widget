package com.essie.myads.ui.home.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.essie.myads.R
import com.essie.myads.common.ui.theme.AppTheme
import com.essie.myads.common.ui.theme.NotoSansFontFamily
import com.essie.myads.domain.entity.AdAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

@ExperimentalCoilApi
@Composable
fun SettingsBody(
    settingsViewModel: SettingsViewModel,
    onConnectClick: () -> Unit,
    onDisconnectClicked: () -> Unit
) {
    val googleAccount by settingsViewModel.googleAccount.observeAsState()
    val selectedAdAccountName by settingsViewModel.selectedAdAccountName.observeAsState("")
    val adAccounts by settingsViewModel.adAccounts.observeAsState(emptyList())

    Column(modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        .verticalScroll(rememberScrollState())
        .semantics { contentDescription = "Settings Screen" }) {

        AccountSettingsContent(onConnectClick, googleAccount, onDisconnectClicked)
        if (googleAccount != null) {
            AdAccountSettingContent(
                selectedAdAccountName,
                adAccounts
            ) { settingsViewModel.selectAdAccount(it) }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun AccountSettingsContent(
    onConnectClick: () -> Unit,
    googleAccount: GoogleSignInAccount?,
    onDisconnectClicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.label_account),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.primary
    )
    if (googleAccount == null) {
        AnonymousProfileRow(onConnectClick)
    } else {
        UserProfileRow(googleAccount, onDisconnectClicked)
    }
}

@Composable
private fun AnonymousProfileRow(onConnectClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.list_button_height))
            .clickable { onConnectClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .background(color = colorResource(R.color.grey_200), shape = CircleShape)
                .border(1.5.dp, colorResource(R.color.grey_700), CircleShape)
                .clip(CircleShape)
                .align(Alignment.CenterVertically),
            painter = painterResource(R.drawable.ic_ghost),
            contentScale = ContentScale.Inside,
            contentDescription = null,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = stringResource(id = R.string.connect_account),
            maxLines = 1,
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp,
            fontFamily = NotoSansFontFamily,
        )
        Spacer(Modifier.width(20.dp))
    }
}

@ExperimentalCoilApi
@Composable
private fun UserProfileRow(account: GoogleSignInAccount, onDisconnectClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.list_button_height)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .background(color = colorResource(R.color.grey_200), shape = CircleShape)
                .border(1.5.dp, colorResource(R.color.green_200), CircleShape)
                .clip(CircleShape)
                .align(Alignment.CenterVertically),
            painter = rememberImagePainter(
                data = account.photoUrl,
                builder = {
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_ghost)
                }
            ),
            contentScale = ContentScale.Inside,
            contentDescription = null,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = account.displayName ?: stringResource(id = R.string.unknown),
            maxLines = 1,
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp,
            fontFamily = NotoSansFontFamily,
        )
        Spacer(Modifier.width(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onDisconnectClicked() }) {
                Image(
                    modifier = Modifier
                        .size(30.dp),
                    contentScale = ContentScale.Inside,
                    painter = painterResource(id = R.drawable.ic_cancel), contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun AdAccountSettingContent(
    selectedAccountName: String,
    adAccounts: List<AdAccount>,
    onSelectItem: (AdAccount) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.list_button_height)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = selectedAccountName,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true }),
            maxLines = 1,
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp,
            fontFamily = NotoSansFontFamily
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.green_200))
        ) {
            adAccounts.forEachIndexed { _, item ->
                DropdownMenuItem(onClick = {
                    onSelectItem(item)
                    expanded = false

                }) {
                    Text(text = item.displayName)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettings() {
    AppTheme {
        AdAccountSettingContent("test", emptyList()) {}
    }
}