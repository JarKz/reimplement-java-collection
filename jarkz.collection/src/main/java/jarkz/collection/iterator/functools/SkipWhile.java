package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class SkipWhile<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private Predicate<T> checker;
  private boolean skipped = false;

  public SkipWhile(Iterator<T> iterator, Predicate<T> checker) {
    this.iterator = iterator;
    this.checker = checker;
  }

  private Optional<T> skip() {
    Optional<T> element;
    while ((element = iterator.next()).isPresent() && checker.test(element.get())) {}

    skipped = true;
    return element;
  }

  @Override
  public Optional<T> next() {
    if (!skipped) {
      return skip();
    }

    return iterator.next();
  }
}
