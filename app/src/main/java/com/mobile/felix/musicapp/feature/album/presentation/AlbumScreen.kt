package com.mobile.felix.musicapp.feature.album.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.felix.musicapp.R
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.presentation.ErrorView
import com.mobile.felix.musicapp.core.presentation.LoadingView
import com.mobile.felix.musicapp.core.presentation.SongItem
import com.mobile.felix.musicapp.feature.album.presentation.action.AlbumAction

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    albumId: Long,
    albumName: String,
    albumPoster: String,
    artistName: String,
    onBackPress: () -> Unit
) {
    val viewModel: AlbumViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.submitAction(AlbumAction.SearchAlbum(albumId))
    }

    AlbumScreenContent(
        state = state.value,
        albumName = albumName,
        albumPoster = albumPoster,
        artistName = artistName,
        modifier = modifier,
        onClickRetry = {
            viewModel.submitAction(AlbumAction.SearchAlbum(albumId))
        },
        onBackPress = onBackPress
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumScreenContent(
    state: AlbumState,
    albumName: String,
    albumPoster: String,
    artistName: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit,
    onBackPress: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(albumName) },
                navigationIcon = {
                    IconButton(onClick = { onBackPress() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(albumPoster)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.cd_artwork, albumName),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    text = albumName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = artistName,
                    fontSize = 12.sp,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                when {
                    state.isLoading -> LoadingView()
                    state.songs != null -> AlbumList(songs = state.songs)
                    state.isUnknowError || state.isInternetError -> ErrorView(
                        isInternetError = state.isInternetError,
                        isUnknowError = state.isUnknowError,
                        onClickRetry = onClickRetry
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumList(
    songs: List<Song>
) {
    if (songs.isEmpty()) {
        Text(
            text = stringResource(R.string.msg_no_songs_album),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(songs.size) { index ->
                val song = songs[index]
                SongItem(song = song)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AlbumScreenContent(
        state = AlbumState(),
        albumName = "Album name",
        albumPoster = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/1c/8e/0b/1c8e0b9a-7d9f-2a3c-6c8b-5d9e7f1a3e7b/886448652422.jpg/100x100bb.jpg",
        artistName = "Artist name",
        onClickRetry = { },
        onBackPress = { }
    )
}