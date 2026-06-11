package com.mobile.felix.musicapp.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.felix.musicapp.R
import com.mobile.felix.musicapp.core.domain.Song

@Composable
fun SongItem(
    song: Song,
    hasMoroOptions: Boolean = false,
    onItemClick: (Long) -> Unit = {},
    onMoreClick: (String, String, String, String, Long) -> Unit = { _, _, _, _, _ -> }
) {
    val labelEmpty = stringResource(R.string.label_empty)
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onItemClick(song.trackId ?: 0L) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.smallPoster)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.cd_artwork, song.trackName ?: ""),
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
                    text = song.trackName ?: song.artistName ?: labelEmpty,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = song.collectionName ?: song.artistName ?: labelEmpty,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            if (hasMoroOptions) {
                IconButton(
                    onClick = {
                        onMoreClick(
                            song.trackName ?: labelEmpty,
                            song.artistName ?: labelEmpty,
                            song.collectionName ?: labelEmpty,
                            song.largePoster ?: "",
                            song.collectionId ?: 0L
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_more),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}