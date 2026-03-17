package com.douglassantana.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.sdui_core.Node
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsável por orquestrar o carregamento da tela SDUI.
 *
 * Chama [FetchScreenUseCase] na inicialização e expõe o estado da UI
 * via [StateFlow], permitindo que a tela reaja a loading, erro e sucesso.
 *
 * ---
 *
 * ViewModel responsible for orchestrating SDUI screen loading.
 *
 * Calls [FetchScreenUseCase] on initialization and exposes UI state
 * via [StateFlow], allowing the screen to react to loading, error, and success.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchScreen: FetchScreenUseCase,
) : ViewModel() {

    private val _node = MutableStateFlow<Node?>(null)
    val node: StateFlow<Node?> = _node

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadScreen()
    }

    private fun loadScreen() {
        viewModelScope.launch {
            _isLoading.value = true
            fetchScreen("/home")
                .onSuccess {
                    _node.value = it
                    _isLoading.value = false
                }
                .onFailure {
                    _error.value = it.message
                    _isLoading.value = false
                }
        }
    }
}
