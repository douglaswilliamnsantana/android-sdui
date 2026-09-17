package com.douglassantana.data.remoteconfig

/**
 * PT: Implementação em memória de [RemoteConfigClient], usada enquanto nenhum projeto
 * Firebase real está configurado. Vem pré-carregada com uma tela SDUI mínima para a chave
 * `"home"`, permitindo testar o fluxo "Firebase Remote Config" de ponta a ponta sem
 * depender do mock server nem de um SDK real.
 *
 * ---
 *
 * In-memory implementation of [RemoteConfigClient], used while no real Firebase project is
 * configured yet. Comes pre-seeded with a minimal SDUI screen for the `"home"` key,
 * allowing the "Firebase Remote Config" flow to be exercised end-to-end without depending
 * on the mock server or a real SDK.
 */
class FakeRemoteConfigClient(
    private val values: Map<String, String> = defaultValues,
) : RemoteConfigClient {

    override suspend fun getString(key: String): String =
        values[key] ?: throw NoSuchElementException("No Remote Config value for key '$key'")

    companion object {
        private val defaultValues = mapOf(
            "home" to """
                {
                  "type": "screen",
                  "props": {},
                  "components": [
                    {
                      "type": "body",
                      "props": {},
                      "components": [
                        {
                          "type": "text",
                          "props": { "text": "Hello from Firebase Remote Config (fake data)!" },
                          "components": []
                        }
                      ]
                    }
                  ]
                }
            """.trimIndent(),
        )
    }
}
