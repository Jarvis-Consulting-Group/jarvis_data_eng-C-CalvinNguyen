package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

  @Override
  public Stream<String> createStrStream(String ... strings) {
    return Stream.of(strings);
  }

  @Override
  public Stream<String> toUpperCase(String ... strings) {
    Stream<String> stringStream = Stream.of(strings);
    return stringStream.map(str -> str.toUpperCase());
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(s -> pattern.equals(s));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    return IntStream.of(arr);
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    List<E> list = stream.collect(Collectors.toList());
    return list;
  }

  @Override
  public List<Integer> toList(IntStream intStream) {
    List<Integer> list = intStream.boxed().collect(Collectors.toList());
    return list;
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    return IntStream.range(start, end);
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    DoubleStream doubleStream = intStream.asDoubleStream();
    return doubleStream.map(d -> Math.sqrt(d));
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    return intStream.filter(i -> i % 2 != 0);
  }

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    Consumer<String> consumer = new Consumer<String>() {

      @Override
      public void accept(String s) {
        System.out.println(prefix + "" + s + "" + suffix);
      }
    };
    return consumer;
  }

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    Stream<String> stringStream = createStrStream(messages);
    stringStream.forEach(s -> printer.accept(s));
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    intStream.filter(i -> i % 2 != 0).forEach(i -> printer.accept(String.valueOf(i)));
  }

  @Override
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
    return ints.flatMap(i -> i.stream()).map(i -> i*i);
  }
}
