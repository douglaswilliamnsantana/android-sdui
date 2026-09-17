package com.douglassantana.domain.error

/**
 * Erros de domínio retornados por [com.douglassantana.domain.repository.SduiRepository] /
 * [com.douglassantana.domain.usecase.FetchScreenUseCase].
 *
 * Desacopla a apresentação da exceção técnica que originou a falha: a camada de UI decide
 * a mensagem a partir do tipo (`is SduiError.Timeout`, etc.), nunca de `exception.message`
 * de uma exceção de infraestrutura (ex: Ktor, serialização), que pode ser técnica demais
 * ou variar entre plataformas.
 *
 * ---
 *
 * Domain errors returned by [com.douglassantana.domain.repository.SduiRepository] /
 * [com.douglassantana.domain.usecase.FetchScreenUseCase].
 *
 * Decouples the presentation layer from the technical exception that caused the failure:
 * the UI layer decides the message from the type (`is SduiError.Timeout`, etc.), never from
 * the `exception.message` of an infrastructure exception (e.g. Ktor, serialization), which
 * can be too technical or vary across platforms.
 */
sealed class SduiError(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class Timeout(cause: Throwable? = null) :
        SduiError("A requisição demorou demais. Tente novamente.", cause)

    class Serialization(cause: Throwable? = null) :
        SduiError("Não foi possível interpretar a resposta do servidor.", cause)

    class Unknown(cause: Throwable? = null) :
        SduiError("Ocorreu um erro inesperado. Tente novamente.", cause)
}
