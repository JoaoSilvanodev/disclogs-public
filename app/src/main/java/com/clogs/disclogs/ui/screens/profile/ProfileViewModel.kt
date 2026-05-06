package com.clogs.disclogs.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clogs.disclogs.data.model.Profiles
import com.clogs.disclogs.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profiles? = null,
    val errorMessage: String? = null,
    val albumCount: Int = 0,
    val followersCount: Int = 0,
    val isCurrentUser: Boolean = true,
    val isFollowing: Boolean = false

)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    fun loadProfile(targetUserId: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // se for nulo é o
            val isMe = targetUserId == null

            val profileResult = if (isMe) {
                repository.getCurrentUser()
            } else {
                repository.getUserProfile(targetUserId!!)
            }

            val reviewsCountResult = repository.getReviewsCount()

            profileResult.onSuccess { profile ->
                val totReviews = reviewsCountResult.getOrDefault(0)

                if  (!isMe) {
                    val followStatus = repository.checkIfFollowing(profile.id).getOrDefault(false)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profile = profile,
                            albumCount = totReviews,
                            isCurrentUser = false,
                            isFollowing = followStatus
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profile = profile,
                            albumCount = totReviews,
                            isCurrentUser = true
                        )
                    }
                }
            }.onFailure { erro ->
                _uiState.update { it.copy(isLoading = false, errorMessage = erro.message) }
            }
        }
    }

    fun toggleFollow() {
        println("DISCLOGS DEBUG: 1. Botão Follow foi clicado!")
        val currentProfile = _uiState.value.profile ?: return
        if (currentProfile == null) {
            println("DISCLOGS DEBUG: 2. ERRO: currentProfile está NULO. Abortando operação.")
            return
        }

        val currentlyFollowing = _uiState.value.isFollowing
        println("DISCLOGS DEBUG: 3. Status atual: Segue? $currentlyFollowing. Alvo: ${currentProfile.id}")

        viewModelScope.launch {
            val result = if (currentlyFollowing) {
                println("DISCLOGS DEBUG: 4. Tentando dar Unfollow...")
                repository.unfollowUser(currentProfile.id)
            } else {
                println("DISCLOGS DEBUG: 4. Tentando dar Follow...")
                repository.followUser(currentProfile.id)
            }

            result.onSuccess {
                println("DISCLOGS DEBUG: 5. SUCESSO no banco! Atualizando a tela.")
                _uiState.update { it.copy(isFollowing = !currentlyFollowing) }
            }.onFailure { erro ->
                println("DISCLOGS DEBUG: 5. FALHA CRÍTICA no banco: ${erro.message}")
            }
        }
    }
}