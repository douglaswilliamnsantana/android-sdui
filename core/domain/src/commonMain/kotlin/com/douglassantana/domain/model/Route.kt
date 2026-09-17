package com.douglassantana.domain.model

import kotlin.jvm.JvmInline

/**
 * Rota de uma tela SDUI (ex: "/home"), usada por
 * [com.douglassantana.domain.repository.SduiRepository] e
 * [com.douglassantana.domain.usecase.FetchScreenUseCase].
 *
 * Value class em vez de [String] cru: evita erro de digitação silencioso ao espalhar rotas
 * pelas camadas (domain → data → apresentação) e deixa as assinaturas mais expressivas, sem
 * custo em runtime (é inline).
 *
 * ---
 *
 * An SDUI screen route (e.g. "/home"), used by
 * [com.douglassantana.domain.repository.SduiRepository] and
 * [com.douglassantana.domain.usecase.FetchScreenUseCase].
 *
 * A value class instead of a raw [String]: avoids silent typos when routes are threaded
 * across layers (domain → data → presentation) and makes signatures more expressive, with
 * no runtime cost (it's inline).
 */
@JvmInline
value class Route(val path: String) {
    companion object {
        val Home = Route("/home")
    }
}
