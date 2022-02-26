FROM maven:3.6.3-jdk-8

EXPOSE 80

COPY . .

RUN mvn clean package -DskipTests
 
CMD ["java", "-jar", "target/spring-boot-0.0.1-SNAPSHOT.jar"]