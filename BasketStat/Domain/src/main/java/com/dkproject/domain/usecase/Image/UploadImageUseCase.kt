package com.dkproject.domain.usecase.Image

import com.dkproject.domain.repository.ImageRepository

interface UploadImageUseCase {
    suspend operator fun invoke(userUid: String, imageUri: String):Result<String>
}


class UploadImageUseCaseImpl(private val uploadImageRepository: ImageRepository): UploadImageUseCase {
    override suspend fun invoke(userUid: String, imageUri: String): Result<String> =
        uploadImageRepository.uploadProfileImage(userUid = userUid, image = imageUri)
}