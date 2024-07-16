package com.dkproject.data.repository.SignUp

import com.dkproject.domain.repository.SignUpRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): SignUpRepository{
    override suspend fun checkExistUser(userUid: String): Result<Boolean> {
        return try {
            val snapshot = fireStore.collection("users").whereEqualTo("userUid", userUid).get().await()
            if(snapshot.isEmpty)
                Result.success(false)
            else
                Result.success(true)
        } catch (e: Exception){
                Result.failure(e)
        }
    }
}