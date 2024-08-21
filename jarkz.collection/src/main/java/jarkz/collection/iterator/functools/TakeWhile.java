package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class TakeWhile<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private Predicate<T> checker;
  private boolean failed = false;

  public TakeWhile(Iterator<T> iterator, Predicate<T> checker) {
    this.iterator = iterator;
    this.checker = checker;
  }

  @Override
  public Optional<T> next() {
    if (failed) {
      return Optional.empty();
    }

    Optional<T> element;

    if ((element = iterator.next()).isPresent() && checker.negate().test(element.get())) {
      failed = true;
      return Optional.empty();
    }

    return element;
  }
}
