FROM tomcat
MAINTAINER vvmurthy@usc.edu

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git

RUN apt-get -y install software-properties-common

RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y --allow-unauthenticated oracle-java8-installer 

RUN apt-get update && \
    apt-get install -yq --no-install-recommends wget ca-certificates expect && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Set up Maven
RUN apt-get update
RUN apt-get install -y maven

# Try like hell to set up SSL
RUN mkdir keys
RUN ls
RUN keytool -genkey -noprompt -storepass changeit -keypass changeit -alias imhungry2 -keyalg RSA -keystore /usr/local/tomcat/keys/client.jks -validity 360 -dname "CN=vvmurthy.usc.edu, OU=ID, O=USC, L=WhoKnows, S=LosAngeles, C=US"
RUN keytool -export -noprompt -keypass changeit -storepass changeit -alias imhungry2 -file /usr/local/tomcat/keys/client.cer -keystore /usr/local/tomcat/keys/client.jks
RUN keytool -export -noprompt -keypass changeit -storepass changeit -alias imhungry2 -file /usr/local/tomcat/keys/client.crt -keystore /usr/local/tomcat/keys/client.jks
# RUN keytool -import -noprompt -storepass changeit -alias imhungry2 -keystore /usr/local/tomcat/keys/client.jks -file /usr/local/tomcat/keys/client.cer
RUN keytool -noprompt -import -keypass changeit -storepass changeit -v -trustcacerts -alias imhungry2 -file /usr/local/tomcat/keys/client.cer -keystore /usr/local/tomcat/keys/clienttrust.jks
RUN keytool -list -v -keystore /usr/local/tomcat/keys/client.jks

RUN ls -a
RUN git clone https://f476d581ef2845cc763b73e6f7500c133c3c5718@github.com/cindyciclta/ImHungry2.git /ImHungry2
WORKDIR /ImHungry2

# Move over server files
RUN cp /ImHungry2/server/server.xml /usr/local/tomcat/conf/server.xml
RUN cp /ImHungry2/server/web.xml /usr/local/tomcat/conf/web.xml
RUN cp /ImHungry2/imhungry.json /usr/local/tomcat/bin

RUN mvn package -DskipTests=true
WORKDIR /ImHungry2/target
RUN cp /ImHungry2/target/ImHungry-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ImHungry.war

WORKDIR /usr/local/tomcat/bin/
RUN wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy
RUN chmod +x cloud_sql_proxy

RUN tail -n +2 "/usr/local/tomcat/bin/catalina.sh" > "/usr/local/tomcat/bin/tempcatalina.sh" && mv "/usr/local/tomcat/bin/tempcatalina.sh" "/usr/local/tomcat/bin/catalina.sh"
RUN echo "#!/usr/bin/env bash \n cloud_sql_proxy -instances=imhungry-1551510643042:us-central1:imhungry=tcp:3306 \
                  -credential_file=/usr/local/tomcat/bin/imhungry.json &" > /usr/local/tomcat/bin/catalinaTemp.sh
RUN cat /usr/local/tomcat/bin/catalina.sh >> /usr/local/tomcat/bin/catalinaTemp.sh
RUN cp /usr/local/tomcat/bin/catalinaTemp.sh /usr/local/tomcat/bin/catalina.sh
RUN chmod +x /usr/local/tomcat/bin/catalina.sh
WORKDIR /usr/local/tomcat
EXPOSE 8080 8443

CMD ["catalina.sh", "run"]