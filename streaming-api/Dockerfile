FROM amazoncorretto:17
ARG CORE_JAR_FILE=build/libs/*.jar
COPY ${CORE_JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8082
