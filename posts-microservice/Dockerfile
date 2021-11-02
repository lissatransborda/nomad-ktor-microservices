FROM alpine:3.14
RUN apk update && apk add openjdk11
RUN mkdir /microservice/
WORKDIR /microservice/
COPY build/libs/com.microservices.posts-microservices-0.0.1-all.jar .
CMD ["java", "-jar", "com.microservices.posts-microservices-0.0.1-all.jar"]
