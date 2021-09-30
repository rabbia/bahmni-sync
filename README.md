# bahmni-sync Development Environment setup

## Prerequisites
 - Docker is installed and running.
 
## Setting up Debezium
 Using Debezium requires three separate services: ZooKeeper, Kafka, and the Debezium connector service. You will set up a single instance of each service using Docker and the Debezium container images.
 
 ### Starting Zookeeper
    docker run -d --name zookeeper -p 2181:2181 jplock/zookeeper
    
 ### Starting Kafka
    docker run -d --name kafka -p 9092:9092 -e KAFKA_ADVERTISED_HOST_NAME=<private-ip> -e ZOOKEEPER_IP=<private-ip> ches/kafka
    
  - replace <private-ip> with your system's private IP
  
 ### Starting a MySQL database
   At this point, you have started ZooKeeper and Kafka, but you still need a database server from which Debezium can capture changes.
 
    docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw debezium/example-mysql
    
  - -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw
     - Creates a user and password that has the minimum privileges required by the Debezium MySQL connector.  
     
  - Import    
     
 ### Starting Kafka Connect
      docker run -d --name connect -p 8083:8083 -e GROUP_ID=1 -e CONFIG_STORAGE_TOPIC=my_connect_configs -e OFFSET_STORAGE_TOPIC=my_connect_offsets -e STATUS_STORAGE_TOPIC=my_connect_statuses --link zookeeper:zookeeper --link kafka:kafka --link mysql:mysql --link mysql:mysql debezium/connect
      
 ### Registering a connector to monitor the openmrs dat abase    
 
 
 
