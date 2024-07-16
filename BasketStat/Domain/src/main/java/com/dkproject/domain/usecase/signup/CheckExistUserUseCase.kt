package com.dkproject.domain.usecase.signup

import com.dkproject.domain.repository.SignUpRepository

interface CheckExistUserUseCase {
    suspend operator fun invoke(userUid: String): Result<Boolean>
}


class CheckExistUserUseCaseImpl(private val signUpRepository: SignUpRepository): CheckExistUserUseCase {
    override suspend fun invoke(userUid: String): Result<Boolean> = signUpRepository.checkExistUser(userUid = userUid)
}