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

RUN mvn test jacoco:report
RUN mvn package -DskipTests=ture
WORKDIR /ImHungry2/target

RUN cat /ImHungry2/target/jacoco-ut/jacoco.csv
RUN cp /ImHungry2/target/ImHungry-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ImHungry.war

WORKDIR /usr/local/tomcat

EXPOSE 8080

CMD ["catalina.sh", "run"]