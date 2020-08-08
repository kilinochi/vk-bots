FROM openjdk:12-jdk-alpine
VOLUME /tmp
COPY build/libs/vk-bots-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 6000
