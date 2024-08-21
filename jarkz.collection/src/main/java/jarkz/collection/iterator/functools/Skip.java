package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;

public class Skip<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private int count;
  private boolean skipped = false;

  public Skip(Iterator<T> iterator, int count) {
    this.iterator = iterator;
    this.count = count;
  }

  private void skip() {
    while (count > 0 && iterator.next().isPresent()) {
      count -= 1;
    }

    skipped = true;
  }

  @Override
  public Optional<T> next() {
    if (!skipped) {
      skip();
    }

    return iterator.next();
  }
}
