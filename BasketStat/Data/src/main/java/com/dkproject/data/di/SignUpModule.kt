package com.dkproject.data.di

import com.dkproject.data.repository.Image.ImageRepositoryImpl
import com.dkproject.data.repository.SignUp.SignUpRepositoryImpl
import com.dkproject.domain.repository.ImageRepository
import com.dkproject.domain.repository.SignUpRepository
import com.dkproject.domain.usecase.Image.UploadImageUseCase
import com.dkproject.domain.usecase.Image.UploadImageUseCaseImpl
import com.dkproject.domain.usecase.signup.CheckExistUserUseCase
import com.dkproject.domain.usecase.signup.CheckExistUserUseCaseImpl
import com.dkproject.domain.usecase.signup.SignUpUseCase
import com.dkproject.domain.usecase.signup.SignUpUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SignUpModule {
    @Binds
    abstract fun bindSignUpRepository(impl: SignUpRepositoryImpl): SignUpRepository

    @Binds
    abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
}

@Module
@InstallIn(SingletonComponent::class)
object SignUpUseCaseModule {
    @Singleton
    @Provides
    fun provideCheckExistUserUseCase(signUpRepository: SignUpRepository): CheckExistUserUseCase {
        return CheckExistUserUseCaseImpl(signUpRepository = signUpRepository)
    }

    @Singleton
    @Provides
    fun provideUploadImageUseCase(imageRepository: ImageRepository) : UploadImageUseCase {
        return UploadImageUseCaseImpl(uploadImageRepository = imageRepository)
    }

    @Singleton
    @Provides
    fun provideSignUpUseCase(signUpRepository: SignUpRepository) : SignUpUseCase {
        return SignUpUseCaseImpl(signUpRepository = signUpRepository)
    }
}