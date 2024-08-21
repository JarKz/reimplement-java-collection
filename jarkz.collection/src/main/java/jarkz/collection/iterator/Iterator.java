package jarkz.collection.iterator;

import jarkz.collection.iterator.functools.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Iterator<T> {
  public Optional<T> next();

  public default Take<T> take(int count) {
    return new Take<>(this, count);
  }

  public default TakeWhile<T> takeWhile(Predicate<T> checker) {
    return new TakeWhile<>(this, checker);
  }

  public default Skip<T> skip(int count) {
    return new Skip<>(this, count);
  }

  public default SkipWhile<T> skipWhile(Predicate<T> checker) {
    return new SkipWhile<>(this, checker);
  }

  public default Chain<T> chain(Iterator<T> otherIterator) {
    return new Chain<>(this, otherIterator);
  }

  public default Filter<T> filter(Predicate<T> checker) {
    return new Filter<>(this, checker);
  }

  public default <R> FilterMap<T, R> filterMap(Function<T, Optional<R>> mapper) {
    return new FilterMap<>(this, mapper);
  }

  public default <R> Map<T, R> map(Function<T, R> mapper) {
    return new Map<>(this, mapper);
  }

  public default <R> FlatMap<T, R> flatMap(Function<T, Iterator<R>> mapper) {
    return new FlatMap<>(this, mapper);
  }

  public default Reverse<T> reverse() {
    // INFO: because of weak type system in Java I (jarkz) can't restrict
    // this method for only types which implements `DoubleEndedIterator` as
    // in rust. So it check in runtime.
    //
    // I can remove default implementation of function and implement for all types,
    // but some types at `jarkz.collection.iterator.functools.*` can't easily implement
    // this method (e.g. Map<T, R> lazily computes and it can't be converted to Reverse<R>).
    throw new UnsupportedOperationException(
        "Using reverse() method to Iterator<T> is not supported. Only for these types which"
            + " implements DoubleEndedIterator<T> can be applied reverse() method");
  }

  public default Optional<T> find(Predicate<T> checker) {
    Optional<T> element;

    while ((element = next()).isPresent() && checker.negate().test(element.get())) {}

    return element;
  }

  public default <R> Optional<R> findMap(Function<T, Optional<R>> mapper) {
    Optional<T> element;
    Optional<R> mappedElement = Optional.empty();

    while ((element = next()).isPresent()
        && (mappedElement = mapper.apply(element.get())).isEmpty()) {}

    return mappedElement;
  }

  public default Optional<T> reduce(BiFunction<T, T, T> combiner) {
    Optional<T> element;
    if ((element = next()).isEmpty()) {
      return element;
    }

    var lval = element.get();
    while ((element = next()).isPresent()) {
      lval = combiner.apply(lval, element.get());
    }

    return Optional.of(lval);
  }

  public default <R> R fold(final R initialValue, BiFunction<R, T, R> accumulator) {
    var result = initialValue;

    Optional<T> element;
    while ((element = next()).isPresent()) {
      result = accumulator.apply(result, element.get());
    }

    return result;
  }

  public default <R> R collect(Collector<T, R> collector) {
    return collector.collect(this);
  }

  public default void forEach(Consumer<T> body) {
    Optional<T> element;
    while ((element = next()).isPresent()) {
      body.accept(element.get());
    }
  }
}
