#
# Build stage
#
FROM maven:3.6.3-jdk-11 AS build
COPY creativedrive-backend /home/app/creativedrive-backend
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11
COPY --from=build /home/app/creativedrive-backend/target/creativedrive-backend.jar /usr/local/lib/creativedrive.jar
EXPOSE 8097
ENTRYPOINT ["java","-jar","/usr/local/lib/creativedrive.jar"]