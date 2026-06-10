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
        startDestination = Router.Home,
        modifier = modifier
    ) {
        composable<Router.Home> {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate(Router.Detail(id))
                },
                onAlbumClicked = { albumName, artist, poster, albumId ->
                    navController.navigate(
                        Router.Album(
                            albumId = albumId,
                            album = albumName,
                            artist = artist,
                            poster = poster
                        )
                    )
                }
            )
        }

        composable<Router.Detail> {
            val id = it.arguments?.getLong("id") ?: 0
            SongScreen(
                id = id,
                onBackPress = {
                    navController.popBackStack()
                },
                onAlbumClicked = { albumName, artist, poster, albumId ->
                    navController.navigate(
                        Router.Album(
                            albumId = albumId,
                            album = albumName,
                            artist = artist,
                            poster = poster
                        )
                    )
                }
            )
        }

        composable<Router.Album> {
            val albumId = it.arguments?.getLong("albumId") ?: 0
            val album = it.arguments?.getString("album") ?: "Empty"
            val poster = it.arguments?.getString("poster") ?: "Empty"
            val artist = it.arguments?.getString("artist") ?: "Empty"

            AlbumScreen(
                albumId = albumId,
                albumName = album,
                albumPoster = poster,
                artistName = artist,
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }
    }
}