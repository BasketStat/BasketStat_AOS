package com.dkproject.data.repository.Image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.dkproject.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val context: Context
):ImageRepository {
    override suspend fun uploadProfileImage(userUid: String, image: String): Result<String> {
        return try {
            val imageStream = context.contentResolver.openInputStream(Uri.parse(image))
            val bitmap = BitmapFactory.decodeStream(imageStream)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP,80,outputStream)
            val imageData = outputStream.toByteArray()

            val metadata = storageMetadata { contentType = "image/webp" }
            val profileImageRef = firebaseStorage.reference.child("ProfileImage").child("uid").child("${userUid}.webp")
            profileImageRef.putBytes(imageData, metadata).await()
            val downloadUrl = profileImageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

}