package com.essie.myads.common.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.essie.myads.common.ui.theme.DialogThemeOverlay

@Composable
fun CommonAlertDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    bodyText: String,
    positiveButtonText: String = "",
    negativeButtonText: String = ""
) {
    DialogThemeOverlay {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = { Text(bodyText) },
            buttons = {
                Column {
                    Divider(
                        Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    TextButton(
                        onClick = onConfirm,
                        shape = RectangleShape,
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(positiveButtonText)
                    }
                    TextButton(
                        onClick = onDismiss,
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(negativeButtonText)
                    }
                }
            }
        )
    }
}