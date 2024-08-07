package com.dkproject.domain.model

data class UserData(
    val userUid: String = "",
    val userNickName: String = "",
    val userProfileImageUrl: String = "",
    val userHeight: Double = 0.0,
    val userWeight: Double = 0.0,
    val userPosition: String = ""
)