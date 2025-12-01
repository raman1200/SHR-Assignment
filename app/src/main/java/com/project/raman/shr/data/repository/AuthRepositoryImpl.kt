package com.project.raman.shr.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.project.raman.shr.R
import com.project.raman.shr.data.datasource.FirebaseDB
import com.project.raman.shr.domain.models.UserData
import com.project.raman.shr.domain.repository.AuthRepository
import com.project.raman.shr.utils.PrefKeys
import com.project.raman.shr.utils.PrefManager
import com.project.raman.shr.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseDB: FirebaseDB
) : AuthRepository {

    override suspend fun googleSignIn(activity: Activity, launcher: ActivityResultLauncher<Intent>?): Flow<Response<UserData>> {
        return flow {
            val credentialManager = CredentialManager.create(activity)
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(activity))
                .build()
            try {
                val result = credentialManager.getCredential(activity, request)
                when (result.credential) {
                    is CustomCredential -> {
                        if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                            val googleTokenId = googleIdTokenCredential.idToken
                            val credential = GoogleAuthProvider.getCredential(googleTokenId, null)
                            val authResult = firebaseDB.signInWithCredential(credential)
                            if (authResult != null) {
                                val firebaseUser = authResult.user!!
                                PrefManager.putString(PrefKeys.USER_PROFILE_NAME, firebaseUser.displayName?:"Unknown")
                                PrefManager.putString(PrefKeys.USER_PROFILE_URL, firebaseUser.photoUrl.toString())
                                PrefManager.putString(PrefKeys.UER_EMAIL, firebaseUser.email?:"")
                                val userData = extractUserData(firebaseUser)
                                emit(Response.Success(userData))
                            } else {
                                emit(Response.Error("Login Failed"))
                            }
                        } else {
                            emit(Response.Error("Login Failed"))
                        }
                    }
                    else -> {
                        emit(Response.Error("Credential Error"))
                    }
                }
            } catch (e: NoCredentialException) {
                launcher?.launch(getAddAccountIntent())
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                emit(Response.Error("Something went wrong"))
                e.printStackTrace()
            }
        }.catch {
            Log.e(TAG, "${it.message}")
            emit(Response.Error("Something went wrong"))
        }
    }

    override fun getCurrentUser(): FirebaseUser? = firebaseDB.getCurrentUser()

    private fun getAddAccountIntent(): Intent {
        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
    }
    private fun getCredentialOptions(context: Context): CredentialOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()
    }
    private fun extractUserData(user: FirebaseUser) = UserData(uid = user.uid, name = user.displayName, email = user.email, image = user.photoUrl.toString())

    companion object {
        private const val TAG = "AuthRepository"
    }
}
