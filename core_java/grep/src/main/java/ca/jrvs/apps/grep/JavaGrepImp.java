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

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;

  /**
   * process() method ties all functionality for JavaGrepImp together: calls listFiles(), pass the
   * rootPath to get a list of files within the directory/subdirectories calls readLines(), pass
   * files one by one from list and gets a list of matched strings calls writeToFile(), pass list of
   * strings, and it will write strings to output file.
   *
   * @throws IOException Throws IOException
   */
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

  /**
   * Given root directory as a String recursively get all files and insert into an ArrayList. use
   * .listFiles() method to get all files and directories. If it is a file, add it to the ArrayList.
   * If it is a directory, recursively call the function and merge any files from the subdirectories
   * to the ArrayList.
   *
   * @param rootDir takes the root directory as a String.
   * @return return an ArrayList of all the files from the directory and subdirectories.
   */
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

  /**
   * Given a file, read each line using BufferedReader, call containsPattern() method if it returns
   * true add the line to the Strings ArrayList and return the ArrayList of matched lines.
   *
   * @param inputFile takes an input File as an argument
   * @return Returns a String ArrayList of matched lines.
   */
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

  /**
   * Uses java.util.regex classes Pattern and Matcher to match each line given.
   *
   * @param line Takes each line of a file as a String
   * @return returns a boolean, true if the line matched, false otherwise.
   */
  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher matcher = pattern.matcher(line);

    return matcher.find();
  }

  /**
   * Given a List of Strings write each String into an output file using BufferedWriter.
   *
   * @param lines Gets a List of strings that matched
   * @throws IOException Throws IOException
   */
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
