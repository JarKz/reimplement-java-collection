package jarkz.collection;

import jarkz.collection.vector.Vector;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class App {

  private Integer[] data =
      IntStream.range(1, Integer.MAX_VALUE / 16).mapToObj(i -> i).toArray(Integer[]::new);

  public static void main(String[] args) throws IOException {
    org.openjdk.jmh.Main.main(args);
  }

  @Benchmark
  public void testStreaming() {

    Arrays.stream(data)
        .map(a -> (long) a * (long) a)
        .filter(a -> a % 2 == 0)
        .mapToLong(a -> a + a)
        .sum();
  }

  @Benchmark
  public void testIterating() {
    new Vector<>(data)
        .intoIterator()
        .map(a -> (long) a * (long) a)
        .filter(a -> a % 2 == 0)
        .reduce((lhs, rhs) -> lhs + rhs);
  }
}
