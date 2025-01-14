FROM amazoncorretto:17-alpine

ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV
#ARG JWT_SECRET

#ENV JWT_SECRET=${JWT_SECRET}
#"-Djwt.secret=${JWT_SECRET}",

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]