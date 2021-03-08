#
# Build stage
#
FROM maven:3.6.3-jdk-11 AS build
COPY autenticacao-backend /home/app/autenticacao-backend
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11
COPY --from=build /home/app/autenticacao-backend/target/autenticacao-backend.jar /usr/local/lib/autenticacao.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/autenticacao.jar"]