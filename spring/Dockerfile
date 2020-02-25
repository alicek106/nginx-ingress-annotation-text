FROM openjdk:8
COPY target/kingsley-0.0.2-SNAPSHOT.jar /kingsley.jar
EXPOSE 80
CMD ["java", "-Dopentracing.jaeger.enable-b3-propagation=true", "-Djava.net.preferIPv4Stack=true", "-jar", "kingsley.jar"]
