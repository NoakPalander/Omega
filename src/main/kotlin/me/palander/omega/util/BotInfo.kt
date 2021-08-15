package me.palander.omega.util

import dev.kord.common.Color
import kotlinx.datetime.Clock
import me.palander.meta.Versions

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