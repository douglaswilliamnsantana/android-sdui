package com.douglassantana.domain.model

/**
 * PT: Fonte de dados de onde uma tela SDUI é buscada. Selecionada pelo usuário (ex: tela de
 * launcher com dois botões) e usada apenas na camada de DI para escolher qual [com.douglassantana.domain.repository.SduiRepository]
 * injetar em [com.douglassantana.domain.usecase.FetchScreenUseCase] — o use case e o
 * ViewModel que o consome nunca precisam saber qual fonte foi escolhida.
 *
 * ---
 *
 * The data source an SDUI screen is fetched from. Selected by the user (e.g. a launcher
 * screen with two buttons) and used only at the DI layer to pick which
 * [com.douglassantana.domain.repository.SduiRepository] to inject into
 * [com.douglassantana.domain.usecase.FetchScreenUseCase] — the use case and the ViewModel
 * consuming it never need to know which source was chosen.
 */
sealed interface ScreenSource {
    data object Backend : ScreenSource
    data object RemoteConfig : ScreenSource
}
