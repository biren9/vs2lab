FROM maven:3.5.3-jdk-10-slim
ADD . /code
EXPOSE 8080
WORKDIR /code
RUN echo "bitter.db.hostname=redis" >> src/main/resources/application.properties &&\
    mvn install package -q -DskipTests
CMD ["java", "-jar", "target/vslab2-0.0.1-SNAPSHOT.jar"]