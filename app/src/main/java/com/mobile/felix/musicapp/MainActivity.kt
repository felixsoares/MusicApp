package com.mobile.felix.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mobile.felix.musicapp.core.presentation.Navigation
import com.mobile.felix.musicapp.feature.splash.SplashScreen
import com.mobile.felix.musicapp.ui.theme.MusicAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showSplash by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(Unit) {
                delay(2000.milliseconds)
                showSplash = false
            }

            if (showSplash) {
                SplashScreen()
            } else {
                MusicAppTheme {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                    ) { innerPadding ->
                        Navigation(
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}