FROM adoptopenjdk/openjdk11
COPY target/interview-task-0.0.1-SNAPSHOT.jar interview-task-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/interview-task-0.0.1-SNAPSHOT.jar"]