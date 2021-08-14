FROM gradle:7.1-jdk16 AS build
COPY --chown=gradle:gradle . /theta
WORKDIR /theta
RUN gradle bin --no-daemon

FROM openjdk:11.0.8-jre-slim
RUN mkdir /config/
COPY --from=build bin/Theta-1.0.0.jar /

ENTRYPOINT ["java", "-jar", "/Theta-1.0.0.jar"]