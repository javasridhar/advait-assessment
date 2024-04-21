package com.sridhar.prashant.advait.assessment.domain.repository

import com.sridhar.prashant.advait.assessment.domain.model.ImageItem

interface ImageRepository {

    suspend fun getImages(url: String) : Result<List<ImageItem>>
}