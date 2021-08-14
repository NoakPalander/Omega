package me.palander.theta

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
import me.palander.theta.util.*

@PrivilegedIntent
@KordPreview
suspend fun main(argv: Array<String>) {
    val (token, wolfram) = tokens(argv)
    println("Starting the Theta bot!")

    val client = Kord(token) {
        intents = Intents(Intent.GuildMessages)
    }.also {
        setupSlashes(it)
    }

    // Bot information and versioning
    val botInfo = BotInfo(
        Versions(bot = "1.0.0", kord = "0.7.4", latex = "1.0.7", serialization = "1.5.0", datetime = "0.2.1",
            kotlin = KotlinVersion.CURRENT.toString()),

        Signature(tag = client.getSelf().tag, color = Color(0x5D3FD3), avatar = client.getSelf().avatar.url)
    )

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
                    value = "[Link](https://github.com/the-programmers-hangout/theta/graphs/contributors)"
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
                    value = "[Github](https://github.com/the-programmers-hangout/theta)"
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