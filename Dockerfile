FROM ubuntu:latest AS build


RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven


WORKDIR /app


COPY . /app


RUN chmod +x mvnw


RUN ./mvnw clean install -U


EXPOSE 8080

ENTRYPOINT ["./mvnw", "spring-boot:run"]