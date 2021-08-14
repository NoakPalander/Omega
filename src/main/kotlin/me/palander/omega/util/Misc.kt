package me.palander.omega.util

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

fun tokens(args: Array<String>): Pair<String, String> {
    val token = args.firstOrNull()
        ?: System.getenv("BOT_TOKEN")
        ?: throw IllegalArgumentException("Require a bot token as 1st start argument or as environment variable!")

    // Wolfram api token
    val wolfram = args.getOrNull(1)?.toString()
        ?: System.getenv("WOLFRAM_TOKEN")?.toString()
        ?: throw IllegalArgumentException("Require a wolfram api-key as 2d start argument or as environment variable!")

    return Pair(token, wolfram)
}

fun Color.toAwt(): java.awt.Color = java.awt.Color(rgb)

fun wolframUrl(input: String) = "https://www.wolframalpha.com/input/?i=${URLEncoder.encode(input, "UTF-8")}"

fun makeQuery(id: String, input: String, format: String = "plaintext", output: String = "json"): String {
    val encoded = URLEncoder.encode(input, "UTF-8")
    return "http://api.wolframalpha.com/v2/query?appid=${id}&input=${encoded}&format=${format}" +
            "&includepodid=Result&output=${output}"
}

@KordPreview
fun StringChoiceBuilder.colorChoices() {
    choice("white", Json.encodeToString(Color(0xFF_FF_FF)))
    choice("black", Json.encodeToString(Color(0x00_00_00)))
    choice("gray", Json.encodeToString(Color(0x36_39_3E)))
    choice("red", Json.encodeToString(Color(0xFF_00_00)))
    choice("blue", Json.encodeToString(Color(0x00_00_FF)))
    choice("green", Json.encodeToString(Color(0x00_FF_00)))
    choice("cyan", Json.encodeToString(Color(0x00_BC_D4)))
    choice("purple", Json.encodeToString(Color(0x5D_3F_D3)))
}