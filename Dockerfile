FROM gradle:7.1-jdk16 AS build
COPY --chown=gradle:gradle . /omega
WORKDIR /omega
RUN gradle bin --no-daemon

FROM openjdk:11.0.8-jre-slim
RUN mkdir /config/
COPY --from=build /omega/bin/Omega-1.0.0.jar /

ENTRYPOINT ["java", "-jar", "/Omega-1.0.0.jar"]