package com.project.raman.shr.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.raman.shr.MainActivity
import com.project.raman.shr.presentation.auth.ui.theme.SHRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if(authViewModel.isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        setContent {
            SHRTheme {
                AuthScreen(
                    onSignInSuccess = {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                )
            }
        }
    }
}
