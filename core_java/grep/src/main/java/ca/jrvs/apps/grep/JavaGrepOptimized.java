package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public interface JavaGrepOptimized {

  void process() throws IOException;

  Stream<File> listFiles(String rootDir);

  Stream<String> readLines(File inputFile);

  boolean containsPattern(String line);

  void writeToFile(Stream<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
