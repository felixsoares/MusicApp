package com.mobile.felix.musicapp.feature.song.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.felix.musicapp.core.domain.Song
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongScreen(
    modifier: Modifier = Modifier
) {
    val song = Song(
        trackId = 1,
        collectionId = 1,
        artistId = 1,
        wrapperType = "track",
        kind = "song",
        artistName = "Artist Name",
        trackName = "Track Name",
        collectionName = "Collection Name",
        largePoster = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/53/a7/7f/53a77fab-c54c-a57b-8130-248fc12d0c80/093624948995.jpg/100x100bb.jpg",
        smallPoster = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/53/a7/7f/53a77fab-c54c-a57b-8130-248fc12d0c80/093624948995.jpg/60x60bb.jpg",
        songPreview = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/53/a7/7f/53a77fab-c54c-a57b-8130-248fc12d0c80/093624948995.jpg/60x60bb.jpg",
        primaryGenreName = "Genre Name"
    )

    Scaffold(
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
                SongSlider()
                SongButtons()
            }
        }
    }
}

@Composable
fun SongButtons(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* do something */ },
                    modifier = Modifier.size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                        containerColor = Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play song",
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.FastRewind,
                        contentDescription = "Fast rewind",
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Fast forward"
                    )
                }
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Repeat,
                    contentDescription = "Next song"
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
fun SongSlider(
    modifier: Modifier = Modifier,
    totalDurationSeconds: Float = 260f
) {
    val minValue = 0f
    var currentSeconds by remember { mutableFloatStateOf(86f) }

    val remainingSeconds = totalDurationSeconds - currentSeconds
    val currentTimeLabel = formatTime(currentSeconds)
    val remainingTimeLabel = "-${formatTime(remainingSeconds)}"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Slider(
            value = currentSeconds,
            valueRange = minValue..totalDurationSeconds,
            onValueChange = { currentSeconds = it },
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

private fun formatTime(secondsInput: Float): String {
    val totalSeconds = secondsInput.toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SongScreen()
}