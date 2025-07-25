package com.example.finad.ui

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.identity.*
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.example.finad.data.remote.AuthService
import com.example.finad.data.remote.ExpenseService
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userName: String?) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<LoginUiState>(LoginUiState.Idle) }
    var isLoading by remember { mutableStateOf(false) }

    fun getNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun startGoogleSignIn() {
        isLoading = true
        uiState = LoginUiState.Loading
        coroutineScope.launch {
            val credentialManager = CredentialManager.create(context)
            val googleSignInOption = GetSignInWithGoogleOption.Builder(
                serverClientId = "171555207555-h31vid2c93o352621f4ckq9latme5vpq.apps.googleusercontent.com"
            ).setNonce(getNonce()).build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleSignInOption)
                .build()
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )
                val credential = result.credential
                if (credential is androidx.credentials.CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        val idToken = googleIdTokenCredential.idToken

                        AuthService.authenticate(idToken) {success, data ->
                        }

                        val name = googleIdTokenCredential.displayName

                        if (idToken != null) {
                            uiState = LoginUiState.Success(name)
                            isLoading = false
                            onLoginSuccess()
                        } else {
                            uiState = LoginUiState.Error("ID token é nulo.")
                            isLoading = false
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        uiState = LoginUiState.Error("Token inválido: ${e.localizedMessage}")
                        isLoading = false
                    }
                } else {
                    uiState = LoginUiState.Error("Credential não reconhecido ou tipo inesperado.")
                    isLoading = false
                }
            } catch (e: GetCredentialException) {
                uiState = LoginUiState.Error(e.localizedMessage ?: "Erro desconhecido ao fazer login.")
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is LoginUiState.Idle -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { startGoogleSignIn() },
                        enabled = !isLoading
                    ) {
                        Text("Entrar com Google")
                    }
                }
            }
            is LoginUiState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginUiState.Success -> {
                Text(
                    text = "Bem-vindo, ${state.userName ?: "usuário"}!",
                    fontSize = 22.sp
                )
            }
            is LoginUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { startGoogleSignIn() }, enabled = !isLoading) {
                        Text("Tentar novamente")
                    }
                }
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}