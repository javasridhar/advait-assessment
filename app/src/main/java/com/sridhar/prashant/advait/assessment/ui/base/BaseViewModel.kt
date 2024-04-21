package com.sridhar.prashant.advait.assessment.ui.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel : LifecycleObserver, ViewModel() {

    val loading: LiveData<Boolean> get() = _loading.distinctUntilChanged()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()

    val error: LiveEvent<String> get() = _error
    private val _error: LiveEvent<String> = LiveEvent()

    protected fun <T> call(
        apiCall: suspend () -> Result<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        handleLoading: Boolean = true,
        handleError: Boolean = true,
    ) = viewModelScope.launch {
        // Show loading
        if (handleLoading) {
            _loading.postValue(true)
        }

        // Execute call
        val result = apiCall.invoke()

        // hide loading
        if (handleLoading) {
            _loading.postValue(false)
        }

        // Check for result
        result.getOrNull()?.let { value ->
            onSuccess?.invoke(value)
        }

        result.exceptionOrNull()?.let { error ->
            onError?.invoke(error)
            if (handleError) {
                onCallError(error)
            }
        }
    }

    private fun onCallError(error: Throwable) {
        setError(error.message.orEmpty())
    }

    protected fun setLoading(isLoading: Boolean) = _loading.postValue(isLoading)

    private fun setError(errorMessage: String) = _error.postValue(errorMessage)
}