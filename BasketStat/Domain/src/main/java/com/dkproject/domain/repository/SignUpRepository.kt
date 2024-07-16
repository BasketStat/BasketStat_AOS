package com.dkproject.domain.repository

interface SignUpRepository {
    suspend fun checkExistUser(userUid: String): Result<Boolean>
}