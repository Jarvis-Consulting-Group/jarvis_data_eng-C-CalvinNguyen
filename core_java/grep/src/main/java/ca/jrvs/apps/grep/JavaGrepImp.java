package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public void process() throws IOException {
    logger.debug("Process Starting: This is a logger debug message.");
    List<File> fileList = listFiles(this.rootPath);
    logger.info(fileList.toString());

    ArrayList<String> matchedLines = new ArrayList<String>();

    for (File file : fileList) {
      matchedLines.addAll(readLines(file));
    }

    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File rootDirectory = new File(rootDir);
    File[] arrayOfFiles = rootDirectory.listFiles();
    ArrayList<File> fileArrayList = new ArrayList<File>();

    if (arrayOfFiles == null) {
      return fileArrayList;
    }

    for (File file : arrayOfFiles) {
      if (file.isDirectory()) {
        fileArrayList.addAll(listFiles(rootDir + "/" + file.getName()));
      } else {
        fileArrayList.add(file);
      }
    }

    return fileArrayList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    logger.info("Reading File: " + inputFile.getName());
    ArrayList<String> linesArrayList = new ArrayList<String>();

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
      String readLine = bufferedReader.readLine();

      while (readLine != null) {
        if (containsPattern(readLine)) {
          linesArrayList.add(inputFile.getName() + ":" + readLine);
        }
        readLine = bufferedReader.readLine();
      }

      bufferedReader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return linesArrayList;
  }

  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.outFile));
    for (String str : lines) {
      logger.info(str);
      bufferedWriter.write(str);
      bufferedWriter.newLine();
    }
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

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }
}
