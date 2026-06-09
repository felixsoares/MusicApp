package com.mobile.felix.musicapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.felix.musicapp.feature.home.presentation.HomeScreen
import com.mobile.felix.musicapp.feature.song.presentation.SongScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Router.Home,
        modifier = modifier
    ) {
        composable<Router.Home> {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate(Router.Detail(id))
                }
            )
        }

        composable<Router.Detail> {
            val id = it.arguments?.getInt("id") ?: 0
            SongScreen(
                id = id
            )
        }
    }
}