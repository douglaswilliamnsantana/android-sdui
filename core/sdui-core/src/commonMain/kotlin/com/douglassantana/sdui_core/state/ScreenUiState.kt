package com.douglassantana.sdui_core.state

/**
 * Representa os possíveis estados de carregamento de uma tela SDUI.
 *
 * Genérico sobre [T], o tipo do dado exibido em caso de sucesso
 * (tipicamente um [com.douglassantana.sdui_core.Node]), permitindo que
 * qualquer ViewModel de tela reutilize o mesmo contrato de estado.
 *
 * ---
 *
 * Represents the possible loading states of an SDUI screen.
 *
 * Generic over [T], the type of data shown on success (typically a
 * [com.douglassantana.sdui_core.Node]), allowing any screen ViewModel
 * to reuse the same state contract.
 */
sealed interface ScreenUiState<out T> {
    data object Loading : ScreenUiState<Nothing>
    data class Success<T>(val data: T) : ScreenUiState<T>
    data class Error(val message: String?) : ScreenUiState<Nothing>
}
