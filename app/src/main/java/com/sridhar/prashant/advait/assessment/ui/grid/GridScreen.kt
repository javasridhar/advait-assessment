package com.sridhar.prashant.advait.assessment.ui.grid

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sridhar.prashant.advait.assessment.R
import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import com.sridhar.prashant.advait.assessment.ui.base.AppComposable
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.downloader.ImageDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GridScreen(
    url: String,
    viewModel: GridViewModel = hiltViewModel()
) {
    val images by viewModel.images.observeAsState(initial = emptyList())
    viewModel.getImages(url)
    AppComposable(
        viewModel = viewModel,
        content = { GridScreenContent(images) }
    )
}

@Composable
fun GridScreenContent(
    images: List<ImageItem>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(Constants.GRID_COLUMN_COUNT)) {
        items(images) { image ->
            Card(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
                        val url = image.thumbnail.getImageUrl()
                        val context = LocalContext.current
                        var exception by remember { mutableStateOf<Exception?>(null) }

                        LaunchedEffect(key1 = url, block = {
                            withContext(Dispatchers.IO) {
                                try {
                                    bitmap = ImageDownloader
                                        .with()
                                        .memoryCache(true)
                                        .diskCache(true, context)
                                        .loadBitmap(url)
                                } catch (e: Exception) {
                                    exception = e
                                }
                            }
                            withContext(Dispatchers.Main) {
                                imageBitmap = bitmap?.asImageBitmap()
                            }
                        })

                        val imageId: Int
                        if (imageBitmap == null) {
                            if (exception != null) {
                                CircleIndicator()
                                imageId = R.drawable.image_load_error
                                DisplayErrorOrPlaceHolderImage(id = imageId)
                            } else {
                                imageId = R.drawable.image_placeholder
                                DisplayErrorOrPlaceHolderImage(id = imageId)
                                CircleIndicator(true)
                            }
                        } else {
                            CircleIndicator()
                            DisplayImage(imageBitmap!!)
                        }
                    }
            }
        }
    }
}

@Composable
fun CircleIndicator(isVisible: Boolean? = null) {
    isVisible?.let {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DisplayErrorOrPlaceHolderImage(id: Int) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1f)
            .clipToBounds(),
        painter = painterResource(id = id),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        alignment = Alignment.Center,
    )
}

@Composable
fun DisplayImage(imageBitmap: ImageBitmap) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1f)
            .clipToBounds(),
        bitmap = imageBitmap,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}
