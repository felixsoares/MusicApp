package com.mobile.felix.musicapp.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorContentView(
    modifier: Modifier = Modifier,
    message: String,
    hasRetry: Boolean = false,
    onClickRetry: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 16.dp, start = 16.dp)
        )

        if (hasRetry) {
            Button(
                onClick = { onClickRetry() },
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text(text = "Retry")
            }
        }
    }
}