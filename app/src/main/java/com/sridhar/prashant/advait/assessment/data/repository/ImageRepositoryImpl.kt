package com.sridhar.prashant.advait.assessment.data.repository

import com.sridhar.prashant.advait.assessment.data.model.RequestException
import com.sridhar.prashant.advait.assessment.data.service.ApiService
import com.sridhar.prashant.advait.assessment.domain.model.ImageItem
import com.sridhar.prashant.advait.assessment.domain.repository.ImageRepository
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ImageRepository {

    override suspend fun getImages(url: String): Result<List<ImageItem>> {
        var apiResponse: Response<List<ImageItem>>? = null
        var code = 0
        var message = ""
        try {
            apiResponse = apiService.getImages(url)
            if (apiResponse.isSuccessful && apiResponse.code() == HttpURLConnection.HTTP_OK) {
                val imageList = apiResponse.body() ?: emptyList()
                return Result.success(imageList)
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> {
                    message = UnknownHostException::class.java.simpleName
                }
                else -> {
                    code = apiResponse?.code()!!
                    message = "An error occurred!"
                }
            }
        }
        return Result.failure(
            RequestException(
                code = code,
                message = message
            )
        )
    }
}