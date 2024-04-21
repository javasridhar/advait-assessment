package com.sridhar.prashant.advait.assessment.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import com.sridhar.prashant.advait.assessment.domain.usecase.GetImageUseCase
import com.sridhar.prashant.advait.assessment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GridViewModel @Inject constructor(
    private val useCase: GetImageUseCase
) : BaseViewModel() {

    private val _images = MutableLiveData<List<ImageItem>>()
    val images: LiveData<List<ImageItem>> = _images

    fun getImages(url: String) {
        call({
            useCase.getImages(url)
        }, onSuccess = {
            _images.postValue(it)
        })
    }
}