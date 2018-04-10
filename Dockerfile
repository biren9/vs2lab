FROM maven:3.5.3-jdk-10-slim
ADD . /code
EXPOSE 8080
WORKDIR /code
RUN mvn install package
CMD ["java", "-jar", "target/vslab2-0.0.1-SNAPSHOT.jar"]