package com.example.vehiclecompanion.ui.place

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.data.network.DiscoverPlace

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel,
    onPlaceClicked: (DiscoverPlace) -> Unit
) {
    val uiState = viewModel.uiState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is PlacesUiState.Success -> {
                PlacesListScreen(
                    places = uiState.places,
                    onPlaceClicked = onPlaceClicked
                )
            }

            is PlacesUiState.Loading -> {
                PlacesLoadingScreen()
            }

            is PlacesUiState.Error -> {
                ErrorScreen(uiState.message, onRetry = { viewModel.discoverPlaces() })
            }
        }
    }
}

@Composable
fun PlacesListScreen(
    places: List<DiscoverPlace>,
    onPlaceClicked: (DiscoverPlace) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(places.size) { index ->
                val place = places[index]
                PlaceCard(
                    place = place,
                    onClick = { onPlaceClicked(place) }
                )
            }
        }
    }
}

@Composable
fun PlaceCard(place: DiscoverPlace, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = place.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            // TODO add placeholder and error images
//                             placeholder(R.drawable.ic_image_placeholder)
//                             error(R.drawable.ic_error_image)
                        }).build()
                ),
                contentDescription = place.name,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = place.name ?: stringResource(R.string.unknown_place),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = place.categoryDisplayName ?: stringResource(R.string.no_category),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                place.rating?.let {
                    Text(
                        text = stringResource(R.string.rating, it),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceCardPreview() {
    PlaceCard(
        place = DiscoverPlace(
            id = 22606,
            name = "Cincinnati Museum Center",
            url = "https://maps.roadtrippers.com/us/cincinnati-oh/attractions/cincinnati-museum-center",
            rating = 4.5,
            loc = doubleArrayOf(-84.537158, 39.109946),
            categoryDisplayName = "Attractions & Culture",
            imageUrl = "https://atlas-assets.roadtrippers.com/uploads/place_image/image/1026827998/-strip_-quality_60_-interlace_Plane_-resize_320x320_U__-gravity_center_-extent_320x320/place_image-image-2ea174a8-c719-45f8-bc23-d91102a96163.jpg"
        ),
        onClick = {}
    )
}

@Composable
fun PlacesLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.loading))
    }
}

@Composable
fun ErrorScreen(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.discover_places_error, message),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}
