FROM eclipse-temurin:21-jdk-alpine AS builder
RUN apk add --no-cache maven
WORKDIR /app
RUN mkdir -p /root/.m2 && printf '<settings>\n  <mirrors>\n    <mirror>\n      <id>google-maven-central</id>\n      <name>Google Maven Central mirror</name>\n      <url>https://maven-central.storage-download.googleapis.com/maven2/</url>\n      <mirrorOf>central</mirrorOf>\n    </mirror>\n  </mirrors>\n</settings>\n' > /root/.m2/settings.xml
COPY pom.xml .
COPY src/ src/
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
