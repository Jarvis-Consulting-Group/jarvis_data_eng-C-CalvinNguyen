package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface JavaGrep {

  /**
   * Temporary Message
   *
   * @throws IOException Throws IOException
   */
  void process() throws IOException;

  /**
   * Takes a root directory as a String, and returns a list of files within that directory.
   *
   * @param rootDir takes the root directory as a String.
   * @return returns a stream of files from the root directory
   */
  Stream<File> listFiles(String rootDir);

  /**
   * takes a File as an argument and returns a list of Strings (matching lines)
   *
   * @param inputFile takes an input File as an argument
   * @return returns a stream of Strings
   */
  Stream<String> readLines(File inputFile);

  /**
   * Takes a line from the file as a String and returns a boolean whether it matches
   *
   * @param line Takes each line of a file as a String
   * @return returns a boolean whether that line matches
   */
  boolean containsPattern(String line);

  /**
   * Writes a list of strings that matched to an output file.
   *
   * @param lines Gets a Stream of strings that matched
   * @throws IOException Throws IO Exception
   */
  void writeToFile(Stream<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
