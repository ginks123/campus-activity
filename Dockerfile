FROM maven:3.9-eclipse-temurin-11 AS builder
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests -q

FROM tomcat:9-jre11-temurin-jammy
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/campus-activity.war /usr/local/tomcat/webapps/ROOT.war

ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.awt.headless=true"

EXPOSE 8080

CMD ["catalina.sh", "run"]
