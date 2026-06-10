package com.mobile.felix.musicapp.feature.song.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.mobile.felix.musicapp.core.presentation.ErrorContentView
import com.mobile.felix.musicapp.core.presentation.LoadingView
import com.mobile.felix.musicapp.feature.song.presentation.action.SongAction
import java.util.Locale

@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    id: Int
) {
    val viewModel: SongViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.submitAction(SongAction.Load(id))
    }

    SongScreenContent(
        modifier = modifier,
        uiState = state.value,
        action = viewModel::submitAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongScreenContent(
    modifier: Modifier = Modifier,
    uiState: SongUiState,
    action: (SongAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Now playing")
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back page"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingView()
            uiState.song != null -> SongData(
                innerPadding = innerPadding,
                song = uiState.song,
                playbackPosition = uiState.playbackPosition,
                playbackState = uiState.playbackState,
                action = action
            )

            uiState.hasError -> ErrorView()
        }
    }
}

@Composable
private fun ErrorView() {
    val message = "An error occurred while loading the song. Please try again later."
    ErrorContentView(message = message)
}


@Composable
private fun SongData(
    innerPadding: PaddingValues,
    song: Song,
    playbackPosition: Long,
    playbackState: PlaybackState,
    action: (SongAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.largePoster)
                    .crossfade(true)
                    .build(),
                contentDescription = "${song.trackName} artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SongDetail(song)
            SongSlider(playbackPosition = playbackPosition, action = action)
            SongButtons(action = action, playbackState = playbackState)
        }
    }
}

@Composable
private fun SongButtons(
    modifier: Modifier = Modifier,
    playbackState: PlaybackState,
    action: (SongAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isPlaying = playbackState == PlaybackState.Playing
        val isEnable = playbackState != PlaybackState.Error
        val clickAction = if (isPlaying) SongAction.Pause else SongAction.Play
        val icon = if (isPlaying) Icons.Filled.PlayArrow else Icons.Filled.Pause
        val contentDescription = if (isPlaying) "Pause song" else "Play song"

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { action(clickAction) },
                    modifier = Modifier.size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                        containerColor = Color.DarkGray
                    ),
                    enabled = isEnable
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                    )
                }
                IconButton(
                    onClick = { action(SongAction.FastRewind) },
                    enabled = isEnable
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastRewind,
                        contentDescription = "Fast rewind",
                    )
                }
                IconButton(
                    onClick = { action(SongAction.FastForward) },
                    enabled = isEnable
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Fast forward"
                    )
                }
            }
            IconButton(
                onClick = { action(SongAction.Repeat) },
                enabled = isEnable
            ) {
                Icon(
                    imageVector = Icons.Filled.Repeat,
                    contentDescription = "Repeat song"
                )
            }
        }
    }
}

@Composable
private fun SongDetail(song: Song) {
    Text(
        text = song.trackName ?: song.artistName ?: "Empty",
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = Modifier.padding(
            top = 16.dp,
            bottom = 4.dp,
            start = 16.dp,
            end = 16.dp
        )
    )
    Text(
        text = song.collectionName ?: song.artistName ?: "Empty",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongSlider(
    modifier: Modifier = Modifier,
    playbackPosition: Long,
    action: (SongAction) -> Unit,
) {
    val minValue = 0f
    val totalDurationMs = 30000L

    var currentPositionMs by remember { mutableLongStateOf(playbackPosition) }
    val remainingTimeMs = totalDurationMs - currentPositionMs
    val currentTimeLabel = formatTime(currentPositionMs)
    val remainingTimeLabel = "-${formatTime(remainingTimeMs)}"

    LaunchedEffect(playbackPosition) {
        currentPositionMs = playbackPosition
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Slider(
            value = currentPositionMs.toFloat(),
            valueRange = minValue..totalDurationMs.toFloat(),
            onValueChange = {
                currentPositionMs = it.toLong()
            },
            onValueChangeFinished = {
                action(SongAction.SeekTo(currentPositionMs))
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(3.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.LightGray,
                        inactiveTrackColor = Color.DarkGray
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentTimeLabel,
                color = Color.LightGray,
                fontSize = 12.sp
            )
            Text(
                text = remainingTimeLabel,
                color = Color.DarkGray,
                fontSize = 12.sp
            )
        }
    }
}

private fun formatTime(millisecondsInput: Long): String {
    val totalSeconds = millisecondsInput / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SongScreenContent(
        uiState = SongUiState(
            song = Song(
                trackName = "Song name",
                collectionName = "Album name",
                artistName = "Artist name",
                durationTime = 240000L,
                largePoster = null,
                songPreview = null,
                trackId = null,
                collectionId = null,
                artistId = null,
                wrapperType = null,
                kind = null,
                smallPoster = null,
                primaryGenreName = null
            ),
            playbackState = PlaybackState.Playing
        ),
        action = {},
    )
}