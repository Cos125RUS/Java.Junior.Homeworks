FROM openjdk:19

COPY out/artifacts/SchoolDB_jar/untitled.jar /tmp/school.jar
COPY out/artifacts/Runner_jar/untitled.jar /tmp/runner.jar

WORKDIR /tmp

CMD ["java", "-jar", "/tmp/runner.jar"]
#CMD ["java", "-jar", "/tmp/school.jar"]
