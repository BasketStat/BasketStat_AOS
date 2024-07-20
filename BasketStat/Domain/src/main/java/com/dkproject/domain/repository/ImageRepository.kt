package com.dkproject.domain.repository

interface ImageRepository {
    suspend fun uploadProfileImage(userUid: String, image: String): Result<String>
}