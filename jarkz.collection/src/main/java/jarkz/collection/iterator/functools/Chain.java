package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;
import java.util.Optional;

public class Chain<T> implements Iterator<T> {
  private Iterator<T> leftIterator;
  private Iterator<T> rightIterator;

  public Chain(Iterator<T> leftIterator, Iterator<T> rightIterator) {
    this.leftIterator = leftIterator;
    this.rightIterator = rightIterator;
  }

  @Override
  public Optional<T> next() {
    return leftIterator.next().or(() -> rightIterator.next());
  }
}
