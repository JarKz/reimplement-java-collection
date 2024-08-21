package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class FilterMap<T, R> implements Iterator<R> {
  private Iterator<T> iterator;
  private Function<T, Optional<R>> mapper;

  public FilterMap(Iterator<T> iterator, Function<T, Optional<R>> mapper) {
    this.iterator = iterator;
    this.mapper = mapper;
  }

  @Override
  public Optional<R> next() {
    Optional<T> element;
    Optional<R> mappedElement = Optional.empty();

    // iterator.next().map(mapper);
    while ((element = iterator.next()).isPresent()
        && (mappedElement = mapper.apply(element.get())).isEmpty()) {}

    return mappedElement;
  }
}
