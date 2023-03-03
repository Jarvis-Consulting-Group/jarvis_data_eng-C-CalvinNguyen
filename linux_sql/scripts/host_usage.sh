#!/bin/bash

#Arguments: psql_host, psql_port, db_name, psql_user, psql_password
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of CLI arguments passed. (echo msg and exit 1 if it's not equal to 5)
if [ "$#" -ne 5 ]; then
  echo "Illegal number of params"
  exit 1
fi

#Get hardware usage info and hostname and put into variables
hostname=$(hostname -f)
vmstat_mb=$(vmstat --unit M)

#Put specific hardware usage info and timestamp into variables
memory_free=$(echo "$vmstat_mb" | tail -1 | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(vmstat -d | tail -1 | awk '{print $10}' | xargs)
disk_available=$(df -m | egrep "^.*/$" | awk '{print $4}' | xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

#Get the host_id
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')"

#Create PSQL command: Insert hardware usage into host_usage
insert_fields="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)"
insert_values="VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"
insert_stmt="$insert_fields$insert_values"

#Set environment variable of password
export PGPASSWORD=$psql_password

#Insert data into database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?