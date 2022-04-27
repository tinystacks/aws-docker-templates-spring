FROM maven:3.6.3-jdk-8
COPY --from=public.ecr.aws/tinystacks/secret-env-vars-wrapper:latest-x86 /opt /opt
COPY --from=public.ecr.aws/awsguru/aws-lambda-adapter:0.3.2-x86_64 /lambda-adapter /opt/extensions/lambda-adapter

EXPOSE 8000

COPY . .

RUN mvn clean package -DskipTests
 
CMD ["/opt/tinystacks-secret-env-vars-wrapper", "java", "-jar", "target/spring-boot-0.0.1-SNAPSHOT.jar"]