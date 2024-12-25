FROM maven:latest AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean install -Dmaven.test.skip
RUN ls -l /application/target/

FROM bellsoft/liberica-openjre-alpine:21 AS layers
WORKDIR /application
COPY --from=builder /application/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM bellsoft/liberica-openjre-alpine:21
VOLUME /tmp
RUN adduser -S spring-user
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
