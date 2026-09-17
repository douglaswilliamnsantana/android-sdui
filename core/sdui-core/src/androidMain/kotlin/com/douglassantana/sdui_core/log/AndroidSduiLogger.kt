package com.douglassantana.sdui_core.log

import android.util.Log

/**
 * Implementação Android de [SduiLogger], baseada em `android.util.Log`.
 *
 * Android implementation of [SduiLogger], backed by `android.util.Log`.
 */
class AndroidSduiLogger : SduiLogger {
    override fun warn(tag: String, message: String) {
        Log.w(tag, message)
    }
}
