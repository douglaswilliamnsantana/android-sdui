package com.douglassantana.domain.usecase

import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.mapper.NodeMapper
import com.douglassantana.sdui_core.Node
/**
 * Caso de uso responsável por buscar e transformar uma tela SDUI do servidor.
 *
 * Orquestra duas etapas:
 * 1. Busca o [com.douglassantana.model.NodeDto] bruto via [com.douglassantana.domain.repository.SduiRepository]
 * 2. Converte para [Node] via [NodeMapper]
 *
 * Retorna [Result.success] com o [Node] pronto para o [com.douglassantana.sdui_core.registry.ComponentRegistry],
 * ou [Result.failure] em caso de erro de rede ou parsing.
 *
 * ---
 *
 * Use case responsible for fetching and transforming an SDUI screen from the server.
 *
 * Orchestrates two steps:
 * 1. Fetches the raw [com.douglassantana.model.NodeDto] via [com.douglassantana.domain.repository.SduiRepository]
 * 2. Converts it to a [Node] via [NodeMapper]
 *
 * Returns [Result.success] with the [Node] ready for [com.douglassantana.sdui_core.registry.ComponentRegistry],
 * or [Result.failure] on network or parsing errors.
 */
class FetchScreenUseCase(
    private val repository: SduiRepository,
) {
    suspend operator fun invoke(route: String): Result<Node> =
        repository.fetchScreen(route).map { dto -> NodeMapper.toNode(dto) }
}
