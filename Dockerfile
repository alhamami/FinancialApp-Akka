FROM ubuntu

RUN apt-get update -y

RUN apt install mysql-server -y

RUN service mysql start sudo -y

RUN apt-get install wget -y

RUN wget https://corretto.aws/downloads/latest/amazon-corretto-11-x64-linux-jdk.deb

RUN apt-get install java-common

RUN dpkg --install amazon-corretto-11-x64-linux-jdk.deb

RUN wget https://archive.apache.org/dist/kafka/3.0.0/kafka_2.13-3.0.0.tgz

#RUN wget -O- https://apt.corretto.aws/corretto.key

#RUN apt-get install software-properties-common

#RUN add-apt-repository 'deb https://apt.corretto.aws stable main' --yes

#RUN apt-get install -y java-11-amazon-corretto-jdk

RUN tar xzf kafka_2.13-3.0.0.tgz

#RUN mv kafka_2.13-3.0.0 ~

RUN ./kafka_2.13-3.0.0/bin/zookeeper-server-start.sh kafka_2.13-3.0.0/config/zookeeper.properties &

RUN ./kafka_2.13-3.0.0/bin/kafka-server-start.sh kafka_2.13-3.0.0/config/server.properties &

ADD Quote.sql /Quote.sql

ADD my.cnf /etc/mysql/my.cnf

RUN service mysql start

CMD ["mysql" ,"-u" ,"root", "<" ,"Quote.sql"]
