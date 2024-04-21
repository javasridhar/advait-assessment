package com.sridhar.prashant.advait.assessment.di

import com.sridhar.prashant.advait.assessment.data.repository.ImageRepositoryImpl
import com.sridhar.prashant.advait.assessment.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindImageListRepository(imageListRepository: ImageRepositoryImpl): ImageRepository
}