package com.sridhar.prashant.advait.assessment.di

import com.sridhar.prashant.advait.assessment.BuildConfig
import com.sridhar.prashant.advait.assessment.data.service.ApiService
import com.sridhar.prashant.advait.assessment.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    fun provideRetrofit(
        @Named("BaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val headerKey = "Content-Type"
        val headerValue = "application/json"
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor {
                val original = it.request()
                val imageRequestBuilder = original.newBuilder()
                imageRequestBuilder.addHeader(headerKey, headerValue)
                it.proceed(imageRequestBuilder.build())
            }
            .addInterceptor {
                val original = it.request()
                val url = original.url
                    .newBuilder()
                    .build()
                val request = original
                    .newBuilder()
                    .url(url)
                    .build()

                it.proceed(request)
            }
            .callTimeout(Constants.CALL_TIME_OUT, TimeUnit.MINUTES)
            .readTimeout(Constants.READ_TIME_OUT, TimeUnit.MINUTES)
            .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    fun providerApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}