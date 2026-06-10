package com.mobile.felix.musicapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.felix.musicapp.feature.album.presentation.AlbumScreen
import com.mobile.felix.musicapp.feature.home.presentation.HomeScreen
import com.mobile.felix.musicapp.feature.song.presentation.SongScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Router.Album(617154241),
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

        composable<Router.Album> {
            val albumId = it.arguments?.getLong("albumId") ?: 0
            AlbumScreen(
                albumId = albumId,
                albumName = "Album Title",
                albumPoster = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/e8/43/5f/e8435ffa-b6b9-b171-40ab-4ff3959ab661/886443919266.jpg/100x100bb.jpg",
                artistName = "Artist Name"
            )
        }
    }
}