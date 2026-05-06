package com.clogs.disclogs.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Spotify
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

data class AuthUiState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val sessionStatus: StateFlow<SessionStatus> = supabaseClient.auth.sessionStatus

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue, errorMessage = null) }
    }

    fun onUsernameChange(newValue: String) {
        _uiState.update { it.copy(username = newValue, errorMessage = null) }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, errorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue, errorMessage = null) }
    }

    fun loginWithEmail() {

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.loginWithEmail(state.email, state.password)

            result.onSuccess {
                println("DISCLOGS DEBUG: Login com sucesso! O Supabase autorizou.")
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                println("DISCLOGS DEBUG: Login com falha! ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }

            }
        }
    }

    fun registerWithEmail() {
        val state = _uiState.value

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "As senhas não coincidem!") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.registerUser(
                email = state.email,
                password = state.password,
                nomeCompleto = state.name,
                nick = state.username
            )
            result.onSuccess {
                println("DISCLOGS DEBUG: registro com sucesso! O Supabase autorizou.")
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                println("DISCLOGS DEBUG: registro com falha! ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loginWithSpotify() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.loginWithSpotify()

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                println("DISCLOGS DEBUG: Falha no Spotify -> ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.loginWithGoogle()

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                println("DISCLOGS DEBUG: Falha no Google -> ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

}
