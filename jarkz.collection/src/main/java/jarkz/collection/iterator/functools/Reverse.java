package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.DoubleEndedIterator;
import java.util.Optional;

public class Reverse<T> implements DoubleEndedIterator<T> {
  private DoubleEndedIterator<T> iterator;

  public Reverse(DoubleEndedIterator<T> iterator) {
    this.iterator = iterator;
  }

  @Override
  public Optional<T> next() {
    return iterator.nextBack();
  }

  @Override
  public Optional<T> nextBack() {
    return iterator.next();
  }
}
