package com.dkproject.data.di

import com.dkproject.data.repository.SignUp.SignUpRepositoryImpl
import com.dkproject.domain.repository.SignUpRepository
import com.dkproject.domain.usecase.signup.CheckExistUserUseCase
import com.dkproject.domain.usecase.signup.CheckExistUserUseCaseImpl
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
}

@Module
@InstallIn(SingletonComponent::class)
object SignUpUseCaseModule {
    @Singleton
    @Provides
    fun provideCheckExistUserUseCase(signUpRepository: SignUpRepository): CheckExistUserUseCase {
        return CheckExistUserUseCaseImpl(signUpRepository = signUpRepository)
    }
}