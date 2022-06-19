Prerequisites

Before you continue, ensure you meet the following requirements:

* You have installed Docker.
* You have installed Maven.

## Step 1: Run Maven ##
    mvn clean install
## Step 2: Build Docker ##
    docker build -t admin-service * .
## Step 3: Run mysql container ##
    docker run -p 3307:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=tripro123  -e MYSQL_DATABASE=dev mysql:8
## Step 4: create a network ##
    docker network create spring-net
## Step 5: link network with mysql ##
    docker network connect spring-net mysqldb
## Step 6: Run Docker Container ##
    docker run -p 8080:8080 --name admin-service --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USERNAME=root -e MYSQL_PASSWORD=tripro123 -e MYSQL_PORT=3306 -e MYSQL_DATABASE=dev admin-service 
