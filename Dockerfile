FROM gradle:7.0-jdk16 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bin --no-daemon

FROM openjdk:11.0.8-jre-slim
RUN mkdir /home/app
ENV HOME=/home/app
WORKDIR $HOME
RUN mkdir /data/

COPY --from=build /home/gradle/bin/Theta-*.jar $HOME/Theta.jar

ENTRYPOINT [ "java", "-jar", "Theta.jar" ]
