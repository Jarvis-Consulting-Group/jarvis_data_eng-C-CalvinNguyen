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

#Get overall hardware info and hostname and put into variables
hostname=$(hostname -f)
lscpu_info=$(lscpu)

#Put specific hardware information and timestamp into variables
cpu_number=$(echo "$lscpu_info" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_info" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_info" | egrep "^Model name:" | awk '{$1=$2=""; print $0}' | xargs)
cpu_mhz=$(echo "$lscpu_info" | egrep "^(CPU MHz:)" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_info" | egrep "^(L2 cache:)" | awk '{print substr($3, 1, length($3)-1)}' | xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S")
total_mem=$(egrep '^MemTotal' /proc/meminfo | awk '{print $2}' | xargs)

#Create PSQL command: Insert hardware info into host_info
insert_fields="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)"
insert_values="VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"
insert_stmt="$insert_fields$insert_values"

#Set environment variable of password
export PGPASSWORD=$psql_password

#Insert data into database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?