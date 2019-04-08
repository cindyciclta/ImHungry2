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

RUN git clone https://f476d581ef2845cc763b73e6f7500c133c3c5718@github.com/cindyciclta/ImHungry2.git /ImHungry2
WORKDIR /ImHungry2
RUN cp /ImHungry2/imhungry.json /usr/local/tomcat
RUN mvn package -DskipTests=true
WORKDIR /ImHungry2/target

RUN cat /ImHungry2/target/jacoco-ut/jacoco.csv
RUN cp /ImHungry2/target/ImHungry-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ImHungry.war

WORKDIR /usr/local/tomcat
RUN wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy
RUN chmod +x cloud_sql_proxy
RUN echo -e "./cloud_sql_proxy -instances=imhungry-1551510643042:us-central1:imhungry=tcp:3306 \
                  -credential_file=/usr/local/tomcat/imhungry.json & \n$(cat /usr/local/tomcat/catalina.sh)" > /usr/local/tomcat/catalina.sh

EXPOSE 8080

CMD ["catalina.sh", "run"]