FROM openjdk:8-jdk-alpine
CMD mvn clean install
CMD mvn package
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]


# EXPERIMENTAL: build jar within docker def

# Build stage

# FROM maven:3.6.3-jdk-11-slim AS build
# COPY * /
# RUN mvn -f /pom.xml install -e
# RUN mvn -f /pom.xml clean package

# #
# # Package stage

# FROM openjdk:8-jdk-alpine
# COPY --from=build target /target
# ARG JAR_FILE=target/*.jar
# COPY --from=build ${JAR_FILE} app.jar
# EXPOSE 8000
# ENTRYPOINT ["java","-jar","/app.jar"]
