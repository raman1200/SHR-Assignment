package com.project.raman.shr.data.datasource

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDB @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun signInWithCredential(credential: AuthCredential) = auth.signInWithCredential(credential).await()
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
