package com.arkivanov.mvikotlin.plugin.idea.timetravel

import com.arkivanov.mvikotlin.timetravel.proto.internal.StoreEventType
import com.arkivanov.mvikotlin.timetravel.proto.internal.TimeTravelEvent
import com.arkivanov.mvikotlin.timetravel.proto.internal.value.type
import java.io.BufferedReader
import java.io.IOException
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

internal fun exec(params: List<String>): Process = Runtime.getRuntime().exec(params.toTypedArray())

@Throws(IOException::class)
internal fun Process.readError(): String? = errorStream?.bufferedReader()?.use(BufferedReader::readText)

internal fun runOnUiThread(block: () -> Unit) {
    if (SwingUtilities.isEventDispatchThread()) {
        block()
    } else {
        SwingUtilities.invokeLater(block)
    }
}

internal fun log(text: String) {
    println("[MVIKotlin]: $text")
}

internal val TimeTravelEvent.description: String
    get() = "[$storeName]: ${type.title}.${value.type}"

internal val StoreEventType.isDebuggable: Boolean
    get() =
        when (this) {
            StoreEventType.INTENT,
            StoreEventType.ACTION,
            StoreEventType.RESULT,
            StoreEventType.LABEL -> true
            StoreEventType.STATE -> false
        }

internal fun showError(text: String) {
    JOptionPane.showMessageDialog(JFrame(), text, "MVIKotlin", JOptionPane.ERROR_MESSAGE)
}
