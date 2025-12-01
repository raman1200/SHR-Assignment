package com.project.raman.shr.domain.repository

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.auth.FirebaseUser
import com.project.raman.shr.domain.models.UserData
import com.project.raman.shr.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun googleSignIn(activity: Activity, launcher: ActivityResultLauncher<Intent>?): Flow<Response<UserData>>
    fun getCurrentUser(): FirebaseUser?
}
