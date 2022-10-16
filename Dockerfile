FROM openjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8082

ARG PROFILE=prod
ENV PROFILE_OPTIONS=$PROFILE

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=$PROFILE_OPTIONS -jar app.jar"]