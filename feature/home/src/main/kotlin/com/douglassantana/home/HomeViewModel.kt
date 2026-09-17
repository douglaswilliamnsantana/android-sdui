package com.douglassantana.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglassantana.domain.model.Route
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.state.ScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por orquestrar o carregamento da tela SDUI.
 *
 * Chama [FetchScreenUseCase] na inicialização e expõe o estado da UI
 * via [StateFlow] de [ScreenUiState], permitindo que a tela reaja a
 * loading, erro e sucesso.
 *
 * ---
 *
 * ViewModel responsible for orchestrating SDUI screen loading.
 *
 * Calls [FetchScreenUseCase] on initialization and exposes UI state
 * via a [StateFlow] of [ScreenUiState], allowing the screen to react to
 * loading, error, and success.
 */
class HomeViewModel(
    private val fetchScreen: FetchScreenUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScreenUiState<Node>>(ScreenUiState.Loading)
    val uiState: StateFlow<ScreenUiState<Node>> = _uiState

    init {
        loadScreen()
    }

    private fun loadScreen() {
        viewModelScope.launch {
            _uiState.value = ScreenUiState.Loading
            fetchScreen(Route.Home)
                .onSuccess { _uiState.value = ScreenUiState.Success(it) }
                .onFailure { _uiState.value = ScreenUiState.Error(it.message) }
        }
    }
}
