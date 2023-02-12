package ca.jrvs.apps.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LambdaStreamExcImpTest {

  private LambdaStreamExcImp implement;
  final Logger logger = LoggerFactory.getLogger(LambdaStreamExcImpTest.class);

  @Before
  public void setUp() throws Exception {
    this.implement = new LambdaStreamExcImp();
    BasicConfigurator.configure();
  }

  @Test
  public void createStrStream() {
    String[] strArray = new String[3];
    strArray[0] = "aBc";
    strArray[1] = "DeF";
    strArray[2] = "gHi";
    Stream<String> strStream = implement.createStrStream(strArray);
    strStream.forEach(s -> System.out.println(s));
  }

  @Test
  public void toUpperCase() {
    String[] strArray = new String[3];
    strArray[0] = "aBc";
    strArray[1] = "DeF";
    strArray[2] = "gHi";
    Stream<String> stringStream = implement.toUpperCase(strArray);
    stringStream.forEach(s -> System.out.println(s));
  }

  @Test
  public void filter() {
    String[] strArray = new String[3];
    strArray[0] = "aBc";
    strArray[1] = "DeF";
    strArray[2] = "gHi";
    Stream<String> stringStream = implement.filter(Stream.of(strArray), "aBc");
    stringStream.forEach(s -> Assert.assertEquals(strArray[0], s));
  }

  @Test
  public void createIntStream() {
    int[] intArray = new int[3];
    intArray[0] = 1;
    intArray[1] = 2;
    intArray[2] = 3;
    IntStream intStream = implement.createIntStream(intArray);
    intStream.forEach(i -> System.out.println(i));
  }

  @Test
  public void toList() {
    String[] strArray = new String[3];
    strArray[0] = "aBc";
    strArray[1] = "DeF";
    strArray[2] = "gHi";
    Stream<String> strStream = implement.createStrStream(strArray);
    List<String> stringList = implement.toList(strStream);
    System.out.println(stringList);
  }

  @Test
  public void testToList() {
    IntStream intStream = implement.createIntStream(1, 4);
    List<Integer> list = implement.toList(intStream);
    System.out.println(list);
  }

  @Test
  public void testCreateIntStream() {
    IntStream intStream = implement.createIntStream(1, 4);
    intStream.forEach(i -> System.out.println(i));
  }

  @Test
  public void squareRootIntStream() {
    IntStream intStream = implement.createIntStream(1, 4);
    DoubleStream doubleStream = implement.squareRootIntStream(intStream);
    doubleStream.forEach(d -> System.out.println(d));
  }

  @Test
  public void getOdd() {
    IntStream intStream = implement.createIntStream(1, 4);
    IntStream oddStream = implement.getOdd(intStream);
    oddStream.forEach(i -> System.out.println(i));
  }

  @Test
  public void getLambdaPrinter() {
    Consumer<String> printer = implement.getLambdaPrinter("start>", "<end");
    printer.accept("Message body");
  }

  @Test
  public void printMessages() {
    String[] messages = {"a", "b", "c"};
    Consumer<String> printer = implement.getLambdaPrinter("msg:", "!");
    implement.printMessages(messages, printer);
  }

  @Test
  public void printOdd() {
    IntStream intStream = implement.createIntStream(1, 4);
    Consumer<String> printer = implement.getLambdaPrinter("odd number:", "!");
    implement.printOdd(intStream, printer);
  }

  @Test
  public void flatNestedInt() {
    IntStream intStream1 = implement.createIntStream(1, 4);
    IntStream intStream2 = implement.createIntStream(4, 9);
    List<Integer> intList1 = implement.toList(intStream1);
    List<Integer> intList2 = implement.toList(intStream2);
    ArrayList listList = new ArrayList<>();
    listList.add(intList1);
    listList.add(intList2);
    Stream<List<Integer>> listStream = listList.stream();
    Stream<Integer> integerStream = implement.flatNestedInt(listStream);
    integerStream.forEach(i -> System.out.println(i));
  }
}