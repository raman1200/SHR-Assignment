package com.project.raman.shr.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.raman.shr.domain.models.UserData
import com.project.raman.shr.domain.usecase.GetCurrentUserUseCase
import com.project.raman.shr.domain.usecase.SignInWithGoogleUseCase
import com.project.raman.shr.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _googleSignInState = MutableStateFlow<Response<UserData>>(Response.Init())
    val googleSignInState: StateFlow<Response<UserData>> = _googleSignInState.asStateFlow()

    fun googleSignIn(activity: Activity, launcher: ActivityResultLauncher<Intent>?) {
        viewModelScope.launch {
            signInWithGoogleUseCase.invoke(activity, launcher).collect { response ->
                _googleSignInState.value = response
            }
        }
    }

    fun isUserLoggedIn() = getCurrentUserUseCase.invoke()!=null

}
