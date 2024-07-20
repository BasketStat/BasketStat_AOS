package com.dkproject.domain.repository

import com.dkproject.domain.model.UserData

interface SignUpRepository {
    suspend fun checkExistUser(userUid: String): Result<Boolean>
    suspend fun setUserData(userData: UserData): Result<Boolean>
}