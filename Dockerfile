FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY target/VisitCounter-1.0-SNAPSHOT/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
