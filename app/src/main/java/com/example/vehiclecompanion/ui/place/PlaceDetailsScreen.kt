package com.example.vehiclecompanion.ui.place

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.data.network.Category
import com.example.vehiclecompanion.data.network.Image
import com.example.vehiclecompanion.data.network.Place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    viewModel: PlaceDetailsViewModel
) {
    val uiState = viewModel.uiState

    when (uiState) {
        is PlaceDetailsUiState.Loading -> {
            PlaceDetailLoadingScreen()
        }

        is PlaceDetailsUiState.Error -> {
            PlaceDetailErrorScreen(
                message = uiState.message,
                onRetry = { viewModel.loadPlaceDetails() },
            )
        }

        is PlaceDetailsUiState.Success -> {
            PlaceDetailContent(
                place = uiState.place
            )
        }
    }
}

@Composable
fun PlaceDetailContent(
    place: Place,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = place.image?.url)
                    .apply {
                        crossfade(true)
                    }.build()
            ),
            contentDescription = place.name,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = place.name ?: "Unknown Place",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = place.category?.displayName ?: "No category",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            place.rating?.let {
                Text(
                    text = stringResource(R.string.place_detail_rating, it.toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.place_detail_minimap_placeholder))
            }

            if (!place.url.isNullOrBlank()) {
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(place.url)
                        } catch (_: Exception) {
                            val intent = Intent(Intent.ACTION_VIEW, place.url.toUri())
                            try {
                                context.startActivity(intent)
                            } catch (_: ActivityNotFoundException) {
                                // TODO show error to user
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.place_detail_open_in_browser))
                }
            }
        }
    }
}

@Composable
fun PlaceDetailLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PlaceDetailErrorScreen(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.place_detail_error_message, message),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Preview(showBackground = true, name = "Place Detail Content Preview")
@Composable
fun PlaceDetailContentPreview() {
    MaterialTheme {
        PlaceDetailContent(
            place = Place(
                id = 22606,
                name = "Cincinnati Museum Center",
                url = "https://maps.roadtrippers.com/us/cincinnati-oh/attractions/cincinnati-museum-center",
                rating = 4.5,
                category = Category("Attractions & Culture"),
                image = Image("https://atlas-assets.roadtrippers.com/uploads/place_image/image/1026827998/-strip_-quality_60_-interlace_Plane_-resize_320x320_U__-gravity_center_-extent_320x320/place_image-image-2ea174a8-c719-45f8-bc23-d91102a96163.jpg")
            )
        )
    }
}
