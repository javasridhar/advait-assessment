package com.sridhar.prashant.advait.assessment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import com.sridhar.prashant.advait.assessment.domain.model.Thumbnail
import com.sridhar.prashant.advait.assessment.ui.grid.GridScreen
import com.sridhar.prashant.advait.assessment.ui.grid.GridScreenContent
import com.sridhar.prashant.advait.assessment.ui.theme.AssessmentTheme
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.NavDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssessmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavDestinations.GRID_SCREEN,
                    ) {
                        composable(NavDestinations.GRID_SCREEN) {
                            GridScreen(url = Constants.LIST_URL)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagesPreview() {
    AssessmentTheme {
        GridScreenContent(
            images = listOf(
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
                ImageItem("1", Thumbnail("1.1", "https://cimg.acharyaprashant.org", "images/img-f92c4193-2483-4ab7-aefd-854f022d81a8", "image.jpg")),
            )
        )
    }
}