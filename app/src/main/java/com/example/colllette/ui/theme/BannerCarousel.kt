package com.example.colllette.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerCarousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    pagerHeight: Int = 200 // Height in dp
) {
    val pagerState = rememberPagerState()

    Box(modifier = modifier) {
        HorizontalPager(
            count = imageUrls.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth() // Fill full width of the screen
                .height(pagerHeight.dp) // Set the height
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth() // Fill full width for each card
                    .height(pagerHeight.dp), // Ensure each card maintains the height
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = imageUrls[page],
                        builder = {
                            crossfade(true)
                            // Optional: Use a valid placeholder
                        }
                    ),
                    contentDescription = "Banner Image ${page + 1}",
                    contentScale = ContentScale.Crop, // Maintain aspect ratio while filling width
                    modifier = Modifier
                        .fillMaxWidth() // Make sure the image fills the entire card width
                        .height(pagerHeight.dp) // Adjust the height to the card's size
                )
            }
        }

        // Indicators
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = Color.Gray
        )
    }
}
