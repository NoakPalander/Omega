# Ωmega
<img alt="icon" src="https://raw.githubusercontent.com/NoakPalander/Omega/main/src/main/resources/logo.png" width="200" height="200"/>

## Description
A bot for calculating and evaluating mathematical expressions/equations, and to compile LaTex text into images. Intended for the TPH server, join us at [Discord/TPH](https://discord.gg/programming).

<a href="https://discord.gg/programming">
<img src="https://img.shields.io/discord/244230771232079873?label=The%20Programmers%20Hangout&logo=discord" alt="The Programmers Hangout">
</a>

## Setup
To run the application you need to create a discord bot. This can be done over at [Discord developer portal](https://discord.com/developers/docs/intro).
The OAuth2 panel requires the "bot" option. And at the bot panel, it requires:
- Send Messages
- Use Slash Commands

The application also requires a wolfram alpha api token. This can be done over at [wolfram alpha api](https://products.wolframalpha.com/api/).

Then you can just invite the bot to your server. Finally, clone the repository
```bash
$ git clone https://github.com/NoakPalander/Omega.git
```
## Docker
Docker isn't required to run this application however it could be beneficial. Otherwise, see "Deploying & usage". There's also an image on [Dockerhub](https://hub.docker.com/r/palander/omega)
that could be used instead.

### Building without dockerhub
```bash
$ docker build -t "palander/omega" .
```

### Deploying
```bash
$ docker run --env-file=.env palander/omega:latest
```

### Alternatively building with docker hub
```bash
$ docker-compose up --build
```

## Deploying & usage
### Building
```bash
$ cd Omega
$ ./gradlew bin # Generates an executable jar file in the "bin" directory
```
On Windows instead run the `gradle.bat` file.

### Running
The application needs the bot token and a wolfram alpha api key. These can be supplied either as start arguments or via environment variables. E.g:
```bash
$ java -jar Omega-1.0.0.jar <bot_token> <wolfram_token> 
```
alternatively 
```bash
BOT_TOKEN=<bot_token> WOLFRAM_TOKEN=<wolfram_token> java -jar Omega-1.0.0.jar
```

### Usage
The bot uses slash commands instead of ugly prefixes so that they're easy to use. Currently, there's 2 commands.
#### Math:
<img alt="icon" src="https://raw.githubusercontent.com/NoakPalander/Omega/main/src/main/resources/math.png" width="280" height="100"/>

The math command only have one argument that is required, the `query` argument is the expression/equation you want to solve. 

#### Tex:
<img alt="icon" src="https://raw.githubusercontent.com/NoakPalander/Omega/main/src/main/resources/tex.png" width="270" height="280"/>

The latex command has 4 arguments:
- `input` [required], the text you want to compile into a LaTex image, it's optional to send the input within an inline-code block, but it can help to not escape keys like \\.
- `bg` [optional], the background color of the LaTex image. By default, this is white.
- `fg` [optional], the text color of the LaTex image. By default, this is black.
The foreground argument and the background argument currently support the colors: white, black, gray, red, blue, green, cyan and purple.
- `size` [optional], the size of the text. By default, this is 50.

You can also receive some bot information simply by pinging the bot. `@Omega` will respond with an information-embed.

## Contributing
See [Contributing](https://github.com/NoakPalander/Omega/blob/main/Contributing.md)

## Dependencies
Omega is written in Kotlin running gradle as build system that uses the ShadowJar plugin. The project utilizes the dependencies:
- [Kord](https://github.com/kordlib/kord)
- [JLatexMath](https://github.com/opencollab/jlatexmath)
- [Ktor](https://ktor.io/)
- [Kotlinx serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Kotlinx datetime](https://github.com/Kotlin/kotlinx-datetime)

## License 
**MIT**, see [LICENSE](https://github.com/NoakPalander/Omega/blob/main/LICENSE.md)