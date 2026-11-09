FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/sonarqube-java-demo-1.0-SNAPSHOT.war \
     /usr/local/tomcat/webapps/sonarqube-java-demo.war

EXPOSE 8080
