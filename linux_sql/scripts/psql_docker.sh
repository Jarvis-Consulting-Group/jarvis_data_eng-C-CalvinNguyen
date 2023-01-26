#!/bin/bash

#Script to start|stop|create the docker psql container
#Arguments: cmd (start|stop|create), db_username, db_password
cmd=$1
db_username=$2
db_password=$3

#Start docker if it's not started
sudo systemctl status docker || sudo systemctl start docker

#Get the docker container status
docker container inspect jrvs-psql
container_status=$?

#Switch case for cmd argument
case $cmd in
  create)

    # Check if psql docker container exists. (echo msg and exit 1 if it does)
    if [ $container_status -eq 0 ]; then
      echo 'Container already exists'
      exit 1
    fi

    # Check the # of CLI arguments passed. (echo msg and exit 1 if it's not equal to 3)
    if [ $# -ne 3 ]; then
      echo 'CREATE requires username and password'
      exit 1
    fi

    #Set environment variable of password
    export PGPASSWORD=$db_password

    # Create the docker volume
    docker volume create pgdata

    # Create & start the psql docker container
    docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d \
    -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

    # Exit with code from last command (docker run)
    exit $?
    ;;

  start|stop)

    # Check if psql docker container exists (echo msg and exit 1 if it doesn't)
    if [ $container_status -eq 1 ]; then
      echo 'Container does not exist'
      exit 1
    fi

    # start/stop psql docker container and exit with code from last command (docker container start/stop jrvs-psql)
    docker container $cmd jrvs-psql
    exit $?
    ;;

  *)
    # illegal cmd argument passed, it is not start|stop|create. exit 1
    echo 'Illegal command'
    echo 'Commands: start|stop|create'
    exit 1
    ;;
esac