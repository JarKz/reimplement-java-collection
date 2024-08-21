package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;

public class Take<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private int count;

  public Take(Iterator<T> iterator, int count) {
    this.iterator = iterator;
    this.count = count;
  }

  @Override
  public Optional<T> next() {
    if (count <= 0) {
      return Optional.empty();
    }

    count -= 1;
    return iterator.next();
  }
}
