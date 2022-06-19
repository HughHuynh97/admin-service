FROM openjdk:17.0.2-oraclelinux8
VOLUME /tmp
ADD target/admin-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]