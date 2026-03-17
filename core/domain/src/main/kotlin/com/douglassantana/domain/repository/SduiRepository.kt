package com.douglassantana.domain.repository

import com.douglassantana.model.NodeDto

/**
 * Contrato para busca de telas SDUI do servidor.
 *
 * Definido na camada de domínio — a implementação fica em `core:data`,
 * garantindo que o domínio não dependa de detalhes de infraestrutura.
 *
 * ---
 *
 * Contract for fetching SDUI screens from the server.
 *
 * Defined in the domain layer — the implementation lives in `core:data`,
 * ensuring the domain does not depend on infrastructure details.
 */
interface SduiRepository {
    suspend fun fetchScreen(route: String): Result<NodeDto>
}
