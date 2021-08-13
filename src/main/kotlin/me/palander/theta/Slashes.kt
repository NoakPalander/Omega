package me.palander.theta

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.rest.builder.interaction.embed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import me.palander.theta.util.*
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula
import java.io.File
import java.util.*

@KordPreview
suspend fun InteractionCreateEvent.texSlash() {
    val res = interaction.acknowledgePublic()
    val args = interaction.data.data.options.value!!.associate {
        it.name to it.value.value?.value.toString()
    }

    val input = args["input"]!!.removeSurrounding("`")
    val bg = args["bg"]?.let { Json.decodeFromString(it) } ?: Color(255, 255, 255)
    val fg = args["fg"]?.let { Json.decodeFromString(it) } ?: Color(0, 0, 0)
    val size = args["size"]?.toFloat() ?: 50f

    val path = UUID.randomUUID().toString() + ".png"
    TeXFormula(input).createPNG(TeXConstants.STYLE_SCRIPT, size, path, bg.toAwt(), fg.toAwt())

    res.followUp {
        files.add(path to File(path).inputStream())
    }.also {
        File(path).delete()
    }
}

@KordPreview
suspend fun InteractionCreateEvent.mathSlash(wolfram: String, botInfo: BotInfo, client: Kord) {
    val res = interaction.acknowledgePublic()
    val args = interaction.data.data.options.value!!.associate {
        it.name to it.value.value?.value.toString()
    }

    val input = args["query"]!!.removeSurrounding("`")
    try {
        val ans = HttpClient(CIO).use { httpClient ->
            val response = httpClient.get<HttpResponse>(makeQuery(wolfram, "solve $input"))

            Json.decodeFromString<JsonObject>(String(response.receive<ByteArray>()))["queryresult"]!!
                .jsonObject["pods"]!!
                .jsonArray[0]
                .jsonObject["subpods"]!!
                .jsonArray[0]
                .jsonObject["plaintext"].toString()
        }

        res.followUp {
            embed {
                color = botInfo.signature.color
                thumbnail {
                    url = client.getSelf().avatar.url
                }
                field {
                    name = client.getSelf().tag
                    value = "The results are from the wolfram alpha api and may not be 100% correct."
                }
                field {
                    name = "Question:"
                    value = "> $input"
                    inline = true
                }
                field {
                    name = "Answer:"
                    value = "[${ans.removeSurrounding("\"")}](${wolframUrl(input)})"
                    inline = true
                }
                footer {
                    text = "- Wolfram alpha"
                }
            }
        }
    }
    catch (_: NullPointerException) {
        res.followUp {
            content = "Request failed.."
        }
    }
}

@KordPreview
suspend fun setupSlashes(client: Kord, guildId: Snowflake) {
// Creates the slash commands
    client.slashCommands.createGuildApplicationCommands(guildId) {
        // Latex command
        command("tex", "Sends a compiled LaTex message") {
            // Input argument, the string the user wants to turn into a LaTex image
            string("input", "The LaTex input") {
                required = true
            }
            // The background color
            string("bg", "the background color. By default it's white.") {
                required = false
                colorChoices()
            }
            // The foreground color
            string("fg", "the foreground [text] color. By default it's black.") {
                required = false
                colorChoices()
            }
            // The output size
            int("size", "The size ouf the output. By default it is 50.") {
                required = false
            }
        }

        // Math solver command
        command("math", "Computes an expression/equation using wolframalpha.") {
            // Query argument, the expression/equation to be solved
            string("query", "The expression/equation/search query.") {
                required = true
            }
        }

        // Help command
        command("help", "Displays a help embed with usage.") {}
    }
}