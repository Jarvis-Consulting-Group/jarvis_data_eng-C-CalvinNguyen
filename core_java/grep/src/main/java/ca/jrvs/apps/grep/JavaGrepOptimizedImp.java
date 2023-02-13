package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepOptimizedImp implements JavaGrepOptimized {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public void process() throws IOException {
    //logger.debug(regex + " " + rootPath + " " + outFile);
    Stream<File> fileStream = listFiles(this.rootPath);

    ArrayList<String> stringArrayList = new ArrayList<String>();

    fileStream.forEach(file -> {
      stringArrayList.addAll(readLines(file).collect(Collectors.toList()));
    });

    writeToFile(stringArrayList.stream());
  }

  @Override
  public Stream<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    File[] filesArray = directory.listFiles();
    ArrayList<File> fileArrayList = new ArrayList<File>();

    if (filesArray == null) {
      return Stream.empty();
    }

    Stream.of(filesArray).forEach(file -> {
      if (file.isDirectory()) {
        Stream<File> tempStream = listFiles(rootDir + "/" + file.getName());
        fileArrayList.addAll(tempStream.collect(Collectors.toList()));
      } else {
        fileArrayList.add(file);
      }
    });

    return fileArrayList.stream();
  }

  @Override
  public Stream<String> readLines(File inputFile) {
    //logger.debug("Path: " + inputFile.getPath());
    ArrayList<String> matchedArrayList = new ArrayList<String>();

    try (Stream<String> lineStream = Files.lines(Paths.get(inputFile.getPath()))) {

      lineStream.forEach(lineString -> {
        if (containsPattern(lineString)) {
          matchedArrayList.add(inputFile.getPath() + ":" + lineString);
        }
      });

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return matchedArrayList.stream();
  }

  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher matcher = pattern.matcher(line);

    return matcher.find();
  }

  @Override
  public void writeToFile(Stream<String> lines) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.outFile));

    lines.forEach(line -> {
      try {
        //logger.info(line);
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    bufferedWriter.close();
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    // Default logger config
    BasicConfigurator.configure();

    JavaGrepOptimizedImp javaGrepOptimizedImp = new JavaGrepOptimizedImp();
    javaGrepOptimizedImp.setRegex(args[0]);
    javaGrepOptimizedImp.setRootPath(args[1]);
    javaGrepOptimizedImp.setOutFile(args[2]);

    try {
      javaGrepOptimizedImp.process();
    } catch (Exception ex) {
      javaGrepOptimizedImp.logger.error("Error: Unable to process", ex);
    }
  }
}
