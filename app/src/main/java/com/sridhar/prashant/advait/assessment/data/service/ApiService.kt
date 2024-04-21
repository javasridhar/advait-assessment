package com.sridhar.prashant.advait.assessment.data.service

import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("")
    suspend fun getImages(@Url url: String): Response<List<ImageItem>>
}