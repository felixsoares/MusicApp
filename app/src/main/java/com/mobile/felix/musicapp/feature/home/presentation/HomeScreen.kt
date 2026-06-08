package com.mobile.felix.musicapp.feature.home.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.mobile.felix.musicapp.core.domain.Song

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.fetchSongsByTerm("linkin park")
    }

    HomeScreenContent(state.value, modifier) {
        viewModel.fetchSongsByTerm("linkin park")
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Songs",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(18.dp)
                    .size(24.dp)
            )
        }

        when (state) {
            is HomeUiState.Data -> SongList(songs = state.songs, modifier = modifier)
            is HomeUiState.Loading -> LoadingView()
            else -> ErrorView(state, onClickRetry)
        }
    }
}

@Composable
fun ErrorView(homeUiState: HomeUiState, onClickRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val message = when (homeUiState) {
            is HomeUiState.InternetError -> "No internet connection. Please check your connection and try again."
            is HomeUiState.UnknowError -> "An unknown error occurred. Please try again later."
            else -> "Some error occurred. Please try again later."
        }

        Text(
            text = message,
            modifier = Modifier.padding(end = 16.dp, start = 16.dp)
        )

        Button(
            onClick = { onClickRetry() },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun SongList(songs: List<Song>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(songs.size) { index ->
            val song = songs[index]
            SongItem(song = song)
        }
    }
}

@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.artworkUrl100)
                .crossfade(true)
                .build(),
            contentDescription = "${song.trackName} artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = song.trackName ?: "", fontWeight = FontWeight.Bold)
                Text(text = song.collectionName ?: "", fontSize = 10.sp, color = Color.Gray)
            }
            Image(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Play",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreenContent(
        state = HomeUiState.InternetError, onClickRetry = {}
    )
}