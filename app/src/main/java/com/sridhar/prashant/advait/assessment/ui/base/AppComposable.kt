package com.sridhar.prashant.advait.assessment.ui.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sridhar.prashant.advait.assessment.R
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
fun AppComposable(
    viewModel: BaseViewModel,
    content: @Composable () -> Unit
) {
    val isLoading by viewModel.loading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
    if (error != null && error!!.contains(UnknownHostException::class.java.simpleName)) {
        Box {
            SnackBar(error!!)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SnackBar(error: String) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(content = {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val message = stringResource(R.string.turn_on_network, error)
            coroutineScope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long,
                )
                /*when (snackBarResult) {
                    SnackbarResult.ActionPerformed -> {
                        Log.d("Snackbar", "Action Performed")
                    }
                    else -> {
                        Log.d("Snackbar", "Snackbar dismissed")
                    }
                }*/
            }
        }
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
}