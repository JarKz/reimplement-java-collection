package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class Map<T, R> implements Iterator<R> {
  private Iterator<T> iterator;
  private Function<T, R> mapper;

  public Map(Iterator<T> iterator, Function<T, R> mapper) {
    this.iterator = iterator;
    this.mapper = mapper;
  }

  @Override
  public Optional<R> next() {
    return iterator.next().map(mapper);
  }
}
