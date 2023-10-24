FROM eclipse-temurin:17-jdk-focal
VOLUME /tmp
COPY target/*.jar yasuo-app.jar
ENTRYPOINT ["java","-jar","/yasuo-app.jar"]