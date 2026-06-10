package com.mobile.felix.musicapp.feature.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.presentation.AlbumActionSheet
import com.mobile.felix.musicapp.core.presentation.ErrorContentView
import com.mobile.felix.musicapp.core.presentation.LoadingView

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, onItemClick: (Int) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state.value,
        modifier = modifier,
        onClickRetry = {
            viewModel.fetchSongsByTerm("")
        },
        onQueryChanged = { query ->
            viewModel.onQueryChanged(query)
        },
        onItemClick = { song ->
            viewModel.saveSong(song)
            onItemClick(song.trackId ?: 0)
        },
        onAlbumClicked = onAlbumClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onItemClick: (Song) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {

    var queryText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    var isTextFieldVisible by remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        var prevIndex = listState.firstVisibleItemIndex
        var prevOffset = listState.firstVisibleItemScrollOffset
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            isTextFieldVisible = index == 0 ||
                    index < prevIndex ||
                    (index == prevIndex && offset < prevOffset)
            prevIndex = index
            prevOffset = offset
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader()

        AnimatedVisibility(
            visible = isTextFieldVisible,
        ) {
            OutlinedTextField(
                value = queryText,
                onValueChange = { newText ->
                    queryText = newText
                    onQueryChanged(newText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                    )
                },
                shape = RoundedCornerShape(24.dp),
                placeholder = {
                    Text(text = "Search")
                }
            )
        }

        when (state) {
            is HomeUiState.Data -> SongList(
                songs = state.songs,
                listState = listState,
                onItemClick = onItemClick,
                onAlbumClicked = onAlbumClicked
            )

            is HomeUiState.Loading -> LoadingView()
            else -> ErrorView(state, onClickRetry)
        }
    }
}

@Composable
private fun HomeHeader() {
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

        Box(
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(18.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun ErrorView(homeUiState: HomeUiState, onClickRetry: () -> Unit) {
    val message = when (homeUiState) {
        is HomeUiState.InternetError -> "No internet connection. Please check your connection and try again."
        is HomeUiState.UnknowError -> "An unknown error occurred. Please try again later."
        else -> "Some error occurred. Please try again later."
    }
    ErrorContentView(
        message = message,
        hasRetry = true,
        onClickRetry = onClickRetry
    )
}

@Composable
fun SongList(
    songs: List<Song>,
    listState: LazyListState,
    onItemClick: (Song) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {
    if (songs.isEmpty()) {
        Text(
            text = "No songs found, search for another term.",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )
    } else {
        var showActionSheet by remember { mutableStateOf(false) }
        var selectedSong by remember { mutableStateOf("") }
        var selectedArtist by remember { mutableStateOf("") }

        var selectedAlbum by remember { mutableStateOf("") }
        var selectedPoster by remember { mutableStateOf("") }
        var selectedAlbumId by remember { mutableLongStateOf(0L) }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(songs.size) { index ->
                val song = songs[index]
                SongItem(
                    song = song,
                    onItemClick = onItemClick,
                    onMoreClick = { songName, artistName, albumName, poster, albumId ->
                        selectedSong = songName
                        selectedArtist = artistName
                        showActionSheet = true
                        selectedPoster = poster
                        selectedAlbum = albumName
                        selectedAlbumId = albumId
                    })
            }
        }

        AlbumActionSheet(
            isOpen = showActionSheet,
            songName = selectedSong,
            artistName = selectedArtist,
            onDismissRequest = { showActionSheet = false },
            onAlbumClick = {
                showActionSheet = false
                onAlbumClicked(
                    selectedAlbum,
                    selectedArtist,
                    selectedPoster,
                    selectedAlbumId
                )
            }
        )
    }
}

@Composable
fun SongItem(
    song: Song,
    onItemClick: (Song) -> Unit,
    onMoreClick: (String, String, String, String, Long) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onItemClick(song) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.smallPoster)
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
                Text(
                    text = song.trackName ?: song.artistName ?: "Empty",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = song.collectionName ?: song.artistName ?: "Empty",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = {
                    onMoreClick(
                        song.trackName ?: "Empty",
                        song.artistName ?: "Empty",
                        song.collectionName ?: "Empty",
                        song.largePoster ?: "",
                        song.collectionId ?: 0L
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreenContent(
        state = HomeUiState.Data(
            songs = listOf(
                Song(
                    trackId = 1,
                    collectionId = 1,
                    artistId = 1,
                    trackName = "In the End",
                    collectionName = "Hybrid Theory",
                    wrapperType = "track",
                    kind = "song",
                    artistName = "Linkin Park",
                    songPreview = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/3f/cb/c7/3fcbc7cc-0606-7f6e-7fc3-793318cfd1ed/mzaf_16081918663584534594.plus.aac.p.m4a",
                    primaryGenreName = "Rock",
                    largePoster = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/1c/8e/0b/1c8e0b9a-7d9f-2a3c-6c8e-9b1a3d2f0e5b/source/100x100bb.jpg",
                    smallPoster = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/1c/8e/0b/1c8e0b9a-7d9f-2a3c-6c8e-9b1a3d2f0e5b/source/60x60bb.jpg",
                    durationTime = 216294,
                ),
            )
        ),
        onClickRetry = {},
        onQueryChanged = {},
        onItemClick = {},
        onAlbumClicked = { _, _, _, _ -> }
    )
}