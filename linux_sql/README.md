# Linux Cluster Monitoring Agent

## Introduction
The Linux App project is a monitoring agent program designed to gather the hardware information and usage of nodes 
within a Linux cluster. The project's designed for an operations/maintenance team that manages a Linux cluster, 
and to do so require hardware usage records of all their nodes.

The software used for this project includes bash through several bash scripts that execute commands to set up the 
database, gather hardware specifications, 
and hardware usage that is then to be inserted into the database every minute by cron. 
Docker creates a container for the PostgreSQL database to run, 
and Git manages the code base using several techniques like GitFlow.

## Quick Start
1. Execute the psql_docker.sh bash script to start, stop or create the PostgreSQL database in Docker.
    - Syntax
        > bash psql_docker.sh (start|stop|create) db_username db_password
    - Example
        > bash psql_docker.sh create postgres password

---

2. Use the psql command to create the host_agent database.
   - > psql -h localhost -U postgres -c "CREATE DATABASE host_agent;"

---

3. Use the psql command in the terminal, and pass the ddl.sql file to set up the database objects. 
    - Syntax
      > psql -h (IP address) -U (username) -d host_agent -f ddl.sql
    - Example
      > psql -h localhost -U postgres -d host_agent -f ddl.sql

---

4. Execute the host_info.sh bash script that gathers the system's hardware specifications and inserts it 
into the database.
   - Syntax
     > bash host_info.sh psql_host 5432 host_agent psql_user psql_password
   - Example
     > bash host_info.sh localhost 5432 host_agent postgres password

---

5. Execute the host_usage.sh bash script that gathers the system's hardware usage and 
inserts it into the database.
    - Syntax
      > bash host_usage.sh psql_host 5432 host_agent psql_user psql_password
    - Example
      > bash host_usage.sh localhost 5432 host_agent postgres password

---

6. Set up a crontab job to schedule the script's (hardware_usage.sh) execution every minute.
    - Copy the previous bash host_usage.sh command
    - Enter the command into the terminal to edit crontab jobs
      > crontab -e
    - Press i to enter editing mode
    - Input the following command
      - Syntax
        >  ( * * * * * (your previous command with all arguments) > /tmp/host_usage.log )
      - Example
        > ( * * * * * bash host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log )
    - Press ESC to exit editing mode, and type :wq to write and quit
    - List crontab jobs to see if it was added
      > crontab -l

## Implementation

The project is implemented using a database with PostgreSQL and a monitoring agent script.
The database is set up within a Docker container using the bash script (psql_docker.sh), and the database objects 
(tables) are created through a PostgreSQL command that passes the ddl.sql file. 
The monitoring agent consists of two scripts (host_info.sh and host_usage.sh) that gather information about the system 
and insert this data into the database.


## Architecture

> Diagram of the overall design:
> ![Architecture Diagram](./assets/Architecture%20Diagram.png)


## Scripts

- psql_docker.sh
  - Description
    -   The script either creates a Docker container to run a PostgreSQL database along with the necessary alpine 
        dependencies if it does not already exist or starts/stops the docker container with the database if it exists.
  - Usage
    > bash psql_docker.sh <cmd (start|stop|create)> <db_username> <db_password>

---

- host_info.sh
    - Description
      - The script executes commands such as lscpu, hostname, and egrep /proc/meminfo to gather hardware 
        specifications. It then creates an insert statement with the values collected along with a timestamp and 
        finally inputs these values into the database using the psql command with the -c option.
    - Usage
      > bash host_info.sh <psql_host> <psql_port> <db_name> <psql_user> <psql_password>
      
      > bash host_info.sh localhost 5432 host_agent postgres password

---

- host_usage.sh
    - Description
      - The script executes commands such as vmstat, hostname, and df to gather hardware usage. 
        It then creates an insert statement with the values collected along with a timestamp and finally inputs these 
        values into the database using the psql command with the -c option.
    - Usage
        > bash host_usage.sh <psql_host> <psql_port> <db_name> <psql_user> <psql_password>

        > bash host_usage.sh localhost 5432 host_agent postgres password
      
---

- crontab
  - Description
    - The crontab schedules a cron job which executes the host_usage.sh bash script every minute.
  - Usage
    - crontab -e to edit cron jobs
    - Press i to enter edit mode (ESC to exit edit mode)
    - Input command within brackets:
      >( * * * * * bash host_usage.sh psql_host psql_port db_name psql_user psql_password > /tmp/host_usage.log )
    - Type :wq to save and exit (crontab -l in terminal to see if it was added properly)

---

## Database Modeling

| host_info        | Data Type    | Description                               |
|------------------|--------------|-------------------------------------------|
| id               | SERIAL       | The primary key.                          |
| hostname         | VARCHAR      | A string of the hostname.                 |
| cpu_number       | INT          | Int value of cpu cores.                   |
| cpu_architecture | VARCHAR      | A string containing cpu's architecture.   | 
| cpu_model        | VARCHAR      | A string containing cpu's model name.     |
| cpu_mhz          | FLOAT        | Float value of CPU MHZ (speed).           |
| l2_cache         | INT          | Int value of the cache for the cpu (kB)   |
| timestamp        | TIMESTAMP    | Current time in UTC                       | 
| total_mem        | INT          | Int value of the RAM for the system (kB)  |

| host_usage     | Data Type | Description                                                  |
|----------------|-----------|--------------------------------------------------------------|
| timestamp      | TIMESTAMP | Current time in UTC.                                         |
| host_id        | SERIAL    | A foreign key referencing (host_info.id)                     |
| memory_free    | INT       | Int value of RAM free (not used) (MB).                       |
| cpu_idle       | INT       | Int value percentage of time spent idle.                     | 
| cpu_kernel     | INT       | Int value percentage of time spent running kernel code.      |
| disk_io        | INT       | Int value of current disk I/O (input/output aka read/write). |
| disk_available | INT       | Int value of available space for root disk (MB).             |

## Test

Each bash script was tested manually through the execution of the file using the xtrace mode option -x to see the value 
or output of every variable and command.

## Deployment

The project's codebase was managed and distributed onto GitHub, 
and the database was deployed using Docker as the database container environment using the psql_docker.sh script. 
After the database is set up, the host_info.sh script executes to collect the hardware specifications, 
and a crontab job was created to schedule host_usage.sh to run every minute to gather the hardware usage

## Improvements

1. Writing better README.md files
2. Working with Linux more and memorizing commands.
3. Working with awk and bash scripting.