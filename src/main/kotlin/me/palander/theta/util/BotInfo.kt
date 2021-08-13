package me.palander.theta.util

import dev.kord.common.Color
import kotlinx.datetime.Clock
import kotlinx.serialization.*

@Serializable
data class Versions(
    val bot: String,
    val kord: String,
    val latex: String,
    val serialization: String,
    val datetime: String,
    val kotlin: String
) {
    override fun toString() = "```\n" +
        "Bot: $bot\n" +
        "Kord: $kord\n" +
        "Latex: $latex\n" +
        "Serialization: $serialization\n" +
        "Datetime: $datetime\n" +
        "Kotlin: $kotlin\n" +
        "```"
}

@Serializable
data class Signature(
    val tag: String,
    val color: Color,
    val avatar: String,
)

class BotInfo(val versions: Versions, val signature: Signature) {
    private var startTime = Clock.System.now()

    val uptime: String
        get() = "${Clock.System.now() - startTime}"
}