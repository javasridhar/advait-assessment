package com.sridhar.prashant.advait.assessment.domain.usecase

import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import com.sridhar.prashant.advait.assessment.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend fun getImages(url: String) : Result<List<ImageItem>> {
        return imageRepository.getImages(url)
    }
}