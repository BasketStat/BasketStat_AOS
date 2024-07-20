package com.dkproject.domain.model

data class UserData(
    val userUid: String,
    val userNickName: String,
    val userProfileImageUrl: String,
    val userHeight: Double,
    val userWeight: Double,
    val userPosition: String
)