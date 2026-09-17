package com.douglassantana.data.remoteconfig

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeRemoteConfigClientTest {

    @Test
    fun `getString returns seeded value`() = runTest {
        val client = FakeRemoteConfigClient(values = mapOf("home" to """{"type":"screen"}"""))

        assertEquals("""{"type":"screen"}""", client.getString("home"))
    }

    @Test
    fun `getString throws when key is missing`() = runTest {
        val client = FakeRemoteConfigClient(values = emptyMap())

        assertFailsWith<NoSuchElementException> {
            client.getString("missing")
        }
    }

    @Test
    fun `default client has a value for home`() = runTest {
        val client = FakeRemoteConfigClient()

        val raw = client.getString("home")

        assertEquals(true, raw.contains("\"type\": \"screen\""))
    }
}
