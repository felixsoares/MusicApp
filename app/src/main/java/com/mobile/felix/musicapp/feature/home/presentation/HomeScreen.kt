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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.felix.musicapp.R
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.presentation.AlbumActionSheet
import com.mobile.felix.musicapp.core.presentation.ErrorView
import com.mobile.felix.musicapp.core.presentation.LoadingView
import com.mobile.felix.musicapp.core.presentation.SongItem
import com.mobile.felix.musicapp.feature.home.presentation.action.HomeAction

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, onItemClick: (Long) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingItems = viewModel.pagedSongsFlow.collectAsLazyPagingItems()

    HomeScreenContent(
        state = state.value,
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        onClickRetry = {
            val query = state.value.query
            viewModel.submitAction(HomeAction.Search(query))
        },
        onQueryChanged = { query ->
            viewModel.submitAction(HomeAction.Search(query))
        },
        onItemClick = { trackId ->
            viewModel.submitAction(HomeAction.SaveSong(trackId))
            onItemClick(trackId ?: 0L)
        },
        onAlbumClicked = onAlbumClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    lazyPagingItems: LazyPagingItems<Song>,
    onClickRetry: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onItemClick: (Long) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {

    var queryText by remember { mutableStateOf(state.query) }

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

        AnimatedVisibility(visible = isTextFieldVisible) {
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
                        contentDescription = stringResource(R.string.label_search),
                        tint = Color.White,
                    )
                },
                shape = RoundedCornerShape(24.dp),
                placeholder = {
                    Text(text = stringResource(R.string.label_search))
                }
            )
        }

        when {
            state.isLoading -> LoadingView()
            state.isUnknowError || state.isInternetError ->
                ErrorView(state.isInternetError, state.isUnknowError, onClickRetry)

            else -> {
                SongList(
                    query = state.query,
                    songs = state.songs,
                    lazyPagingItems = lazyPagingItems,
                    listState = listState,
                    onItemClick = onItemClick,
                    onAlbumClicked = onAlbumClicked
                )
            }
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
            text = stringResource(R.string.title_songs),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Box(modifier = Modifier) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.label_search),
                modifier = Modifier
                    .padding(18.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun SongList(
    query: String,
    songs: List<Song>?,
    lazyPagingItems: LazyPagingItems<Song>, // 📍 Adicionado
    listState: LazyListState,
    onItemClick: (Long) -> Unit,
    onAlbumClicked: (String, String, String, Long) -> Unit
) {
    var showActionSheet by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf("") }
    var selectedArtist by remember { mutableStateOf("") }
    var selectedAlbum by remember { mutableStateOf("") }
    var selectedPoster by remember { mutableStateOf("") }
    var selectedAlbumId by remember { mutableLongStateOf(0L) }

    val onMoreClickAction: (String, String, String, String, Long) -> Unit =
        { songName, artistName, albumName, poster, albumId ->
            selectedSong = songName
            selectedArtist = artistName
            selectedPoster = poster
            selectedAlbum = albumName
            selectedAlbumId = albumId
            showActionSheet = true
        }

    Box(modifier = Modifier.fillMaxSize()) {
        if (query.isBlank()) {
            if (songs.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.msg_no_songs_saved),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
                ) {
                    items(songs.size, key = { index -> songs[index].trackId ?: index }) { index ->
                        val song = songs[index]
                        SongItem(
                            song = song,
                            onItemClick = onItemClick,
                            onMoreClick = onMoreClickAction
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { song -> song.trackId ?: 0L }
                ) { index ->
                    val song = lazyPagingItems[index]
                    if (song != null) {
                        SongItem(
                            song = song,
                            onItemClick = onItemClick,
                            onMoreClick = onMoreClickAction
                        )
                    }
                }

                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }

        AlbumActionSheet(
            isOpen = showActionSheet,
            songName = selectedSong,
            artistName = selectedArtist,
            onDismissRequest = { showActionSheet = false },
            onAlbumClick = {
                showActionSheet = false
                onAlbumClicked(selectedAlbum, selectedArtist, selectedPoster, selectedAlbumId)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
//    HomeScreenContent(
//        state = HomeUiState(
//            songs = emptyList()
//        ),
//        onClickRetry = {},
//        onQueryChanged = {},
//        onItemClick = {},
//        onAlbumClicked = { _, _, _, _ -> },
//        lazyPagingItems = LazyPagingItems
//    )
}