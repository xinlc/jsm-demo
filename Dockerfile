FROM fabric8/tomcat-7
USER root
RUN rm -rf /opt/apache-tomcat-7.0.70/webapps/*
RUN  mkdir -p /data/wwwlogs/
RUN  touch /data/wwwlogs/demo.log
ADD  ./ /opt/apache-tomcat-7.0.70/webapps/demo/
RUN rm -r /opt/apache-tomcat-7.0.70/conf/server.xml
ADD ./server.xml /opt/apache-tomcat-7.0.70/conf/
EXPOSE 8080

