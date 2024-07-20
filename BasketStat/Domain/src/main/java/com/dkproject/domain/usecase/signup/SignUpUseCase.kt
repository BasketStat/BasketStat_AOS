package com.dkproject.domain.usecase.signup

import com.dkproject.domain.model.UserData
import com.dkproject.domain.repository.SignUpRepository
import kotlin.math.sign

interface SignUpUseCase {
    suspend operator fun invoke(userData: UserData): Result<Boolean>
}

class SignUpUseCaseImpl(private val signUpRepository: SignUpRepository): SignUpUseCase {
    override suspend fun invoke(userData: UserData): Result<Boolean> = signUpRepository.setUserData(userData = userData)
}