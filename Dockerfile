FROM openjdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} interviewPrep-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/interviewPrep-0.0.1-SNAPSHOT.jar"]

