package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp extends JavaGrepImp {

  final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure();

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("Error: unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    logger.debug(super.getRegex() + " " + super.getRootPath() + " " + super.getOutFile());
    List<File> fileList = listFiles(super.getRootPath());

    ArrayList<String> matchedLines = new ArrayList<String>();

    fileList.forEach(file -> matchedLines.addAll(readLines(file)));

    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    File[] arrayOfFiles = directory.listFiles();
    ArrayList<File> fileArrayList = new ArrayList<File>();

    if (arrayOfFiles == null) {
      return fileArrayList;
    }

    Stream.of(arrayOfFiles).forEach(file -> {
      if (file.isDirectory()) {
        fileArrayList.addAll(listFiles(rootDir + "/" + file.getName()));
      } else {
        fileArrayList.add(file);
      }
    });

    return fileArrayList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    logger.debug("Path: " + inputFile.getPath());
    ArrayList<String> matchedArrayList = new ArrayList<String>();

    try (Stream<String> lineStream = Files.lines(Paths.get(inputFile.getPath()))) {

      lineStream.forEach(lineString -> {
        if (super.containsPattern(lineString)) {
          matchedArrayList.add(inputFile.getPath() + ":" + lineString);
        }
      });

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return matchedArrayList;
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(super.getOutFile()));

    lines.forEach(line -> {
      try {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    bufferedWriter.close();
  }
}
