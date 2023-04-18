FROM java:8
EXPOSE 8080

ADD springboot-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]