package me.palander.omega

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.palander.meta.versions
import me.palander.omega.util.*

@PrivilegedIntent
@KordPreview
suspend fun main(argv: Array<String>) {
    val (token, wolfram) = tokens(argv)
    println("Tokens read successfully.. starting Omega!")

    val client = Kord(token) {
        intents = Intents(Intent.GuildMessages)
    }.also {
        setupSlashes(it)
    }

    // Bot information and versioning
    val botInfo = BotInfo(versions, Signature(tag = client.getSelf().tag, color = Color(0x5D3FD3), avatar = client.getSelf().avatar.url))

    client.on<MessageCreateEvent> {
        // If the bot is mentioned, display signature embed
        if (client.selfId in message.mentionedUserIds) {
            message.channel.createEmbed {
                color = botInfo.signature.color
                thumbnail {
                    url = botInfo.signature.avatar
                }
                field {
                    name = botInfo.signature.tag
                    value = "A bot for creating LaTex snippets and evaluating mathematical expressions."
                }
                field {
                    name = "Prefix"
                    value = "/"
                    inline = true
                }
                field {
                    name = "Ping"
                    value = client.gateway.averagePing.toString()
                    inline = true
                }
                field {
                    name = "Contributors"
                    value = "[Link](https://github.com/NoakPalander/Omega/graphs/contributors)"
                    inline = true
                }
                field {
                    name = "Build Info"
                    value = botInfo.versions.toString()
                }
                field {
                    name = "Uptime"
                    value = botInfo.uptime
                    inline = true
                }
                field {
                    name = "Source"
                    value = "[Github](https://github.com/NoakPalander/Omega)"
                    inline = true
                }
            }
        }
    }

    client.on<InteractionCreateEvent> {
        when (interaction.data.data.name.value) {
            "tex" -> texSlash()
            "math" -> mathSlash(wolfram, botInfo, client)
        }
    }

    client.login()
}
