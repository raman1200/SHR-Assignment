package com.project.raman.shr.domain.usecase

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.project.raman.shr.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity, launcher: ActivityResultLauncher<Intent>?) =
        repository.googleSignIn(activity, launcher)
}
