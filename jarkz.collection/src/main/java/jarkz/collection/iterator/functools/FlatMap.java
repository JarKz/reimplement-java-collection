package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class FlatMap<T, R> implements Iterator<R> {
  private Iterator<T> iterator;
  private Function<T, Iterator<R>> mapper;
  private Optional<Iterator<R>> currentIterator = Optional.empty();

  public FlatMap(Iterator<T> iterator, Function<T, Iterator<R>> mapper) {
    this.iterator = iterator;
    this.mapper = mapper;
  }

  @Override
  public Optional<R> next() {
    Optional<R> element;

    if (currentIterator.isPresent() && (element = currentIterator.get().next()).isPresent()) {
      return element;
    }

    while ((currentIterator = iterator.next().map(mapper)).isPresent()) {
      if ((element = currentIterator.get().next()).isPresent()) {
        return element;
      }
    }

    return Optional.empty();
  }
}
