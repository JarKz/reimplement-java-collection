package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class Filter<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private Predicate<T> checker;

  public Filter(Iterator<T> iterator, Predicate<T> checker) {
    this.iterator = iterator;
    this.checker = checker;
  }

  @Override
  public Optional<T> next() {
    Optional<T> element;
    while ((element = iterator.next()).isPresent() && checker.negate().test(element.get())) {}

    return element;
  }
}
