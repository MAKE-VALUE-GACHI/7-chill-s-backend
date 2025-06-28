FROM amazoncorretto:21 as GRADLE_BUILD

WORKDIR /workspace
COPY . .

RUN ./gradlew bootJar

FROM amazoncorretto:21-alpine-jdk

ARG PROFILE=default
ENV ACTIVE_PROFILE=${PROFILE}
ARG JAR_FILE=*.jar

COPY --from=GRADLE_BUILD /workspace/build/libs/${JAR_FILE} /app.jar

ENTRYPOINT ["sh", "-c", "java \
            -XX:InitialRAMPercentage=20 \
            -XX:MaxRAMPercentage=40 \
            -XX:CompileThreshold=200 \
           -Dspring.profiles.active=${ACTIVE_PROFILE} \
           -jar /app.jar"]
EXPOSE 10701
