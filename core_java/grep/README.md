# Java Grep App

## Introduction
The Java Grep app replicates the egrep Linux command with the recursive option so that it reads all 
subdirectories and files. Lastly, it includes the redirection operator, so the output is written to 
a file (egrep -r [regex/pattern] [sourceFile] > [targetFile]). The app was developed using the IDE, 
IntelliJ IDEA, which includes features for building Java applications with Maven. Java.io 
(core java package) was used to read and write to files (Buffered Reader/Writer), Stream API, and 
Lambda functions iterated through directories, lines from a file, or a string stream to write lines 
to a file. Maven builds the project, packages it into a JAR file, and manages dependencies like 
SLF4J for debug logging and JUnit4 for unit testing, which aided with manually testing the app. 
For deploying, We created a Docker image using a DockerFile, distributed it onto Docker Hub, and 
ran it on a Docker container.

## Quick Start
We pull the Docker image from Docker Hub and run a container using the image with 
the necessary volumes so that the app then deploys on the docker container.

Pull Image:
```
docker pull ccalvinnguyen/grep
```

Syntax:
```
docker run -v (source volume) -v (output volume) ccalvinnguyen/grep (regex) (source location) (output location)
```

An example:
```
docker run -v `pwd`/data:/data -v `pwd`/log:log ccalvinnguyen/grep .*Romeo.*Juliet.* /data /log/grep.out
```

## Implementation

### Pseudocode

Process method

- listFIles() method - (gets all files within the directory & subdirectories as a Stream<File>)

- foreach(file -> readLine(file)) - (foreach lambda iterates through stream and calls readLine() 
that returns a Stream<String> of matched lines)

- readLine(forEach(line -> containsPattern(line)) - (iterates through the lines of the file and 
checks if it matches using containsPattern() method)

- writeToFile(stream<string>) - (iterates through the stream of strings and writes each line 
into a file)

## Performance Issue

There may be issues with the Java Heap and throwing OutOfMemoryExceptions when the java heap is 
too small for a large file. For example, we tested it by setting the Java Heap to 5MB, and the 
Shakespeare.txt file was 5.2-5.5MB, and it should throw the OutOfMemoryException when reading the 
file. To fix this, we can parse the file in chunks using Streams and BufferedReader with a 
specific buffer size.

## Test

The Java Grep app was tested manually and used dependencies managed by Maven, like the logging 
package SLF4J to see which files were being read by listFiles, and matched lines from containsPattern.

## Deployment

Using the Dockerfile which contains
```dockerfile
FROM openjdk:8-alpine
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java", "-jar", "/usr/local/app/grep/lib/grep.jar"]
```
We started with the OpenJDK image that had JDK 8 and alpine. We then copied the JAR file into the 
docker container, and lastly, we set an entry point to use a java -jar command to execute the jar 
file when we pass our options to the container.

## Improvement
1. Integrate unit testing using JUnit4 more into the development of the project specifically 
for the methods listFiles(), readLines(), containsPattern(), and writeToFile()

2. Integrate exceptions more into the project, methods such as process() and writeToFile() may 
throw an IOException, but maybe there could be an example of such or how to handle exceptions.

3. Setting up the Maven installation correctly, I had issues where /opt/maven/bin was missing, 
so I downloaded the latest Binary tar.gz archive from maven.apache.org and redid the symlink.