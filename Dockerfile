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

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle


RUN apt-get update
RUN apt-get install -y maven

RUN ls -a
RUN git clone https://f476d581ef2845cc763b73e6f7500c133c3c5718@github.com/cindyciclta/ImHungry2.git /ImHungry2
WORKDIR /ImHungry2
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
EXPOSE 8080

CMD ["catalina.sh", "run"]