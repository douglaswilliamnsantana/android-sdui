package com.douglassantana.sdui_core.registry

import com.douglassantana.sdui_core.log.SduiLogger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapLookupTest {

    private class RecordingLogger : SduiLogger {
        val warnings = mutableListOf<Pair<String, String>>()
        override fun warn(tag: String, message: String) {
            warnings.add(tag to message)
        }
    }

    @Test
    fun `returns value when key is present and does not warn`() {
        val logger = RecordingLogger()
        val map = mapOf("a" to 1)

        val result = map.lookupOrWarn("a", logger, "Tag") { "missing $it" }

        assertEquals(1, result)
        assertEquals(emptyList(), logger.warnings)
    }

    @Test
    fun `returns null and warns when key is absent`() {
        val logger = RecordingLogger()
        val map = mapOf("a" to 1)

        val result = map.lookupOrWarn("b", logger, "Tag") { "missing $it" }

        assertNull(result)
        assertEquals(listOf("Tag" to "missing b"), logger.warnings)
    }
}
