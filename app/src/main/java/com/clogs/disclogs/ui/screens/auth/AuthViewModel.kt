package com.clogs.disclogs.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

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
                println("DISCLOGS DEBUG: Login com sucesso!")
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
                println("DISCLOGS DEBUG: registro com sucesso!")
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


    fun onGoogleSignInResult(idToken: String?) {
        if (idToken == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao obter token do Google") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val loginResult = authRepository.loginWithGoogle(idToken)

            loginResult.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onGoogleSignInError(errorMsg: String?) {
        _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
    }

}
