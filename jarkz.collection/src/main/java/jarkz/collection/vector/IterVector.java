package jarkz.collection.vector;

import jarkz.collection.iterator.DoubleEndedIterator;
import java.util.Optional;

public class IterVector<T> implements DoubleEndedIterator<T> {
  private Vector<T> data;
  private int frontPointer = 0;
  private int backPointer;

  public IterVector(Vector<T> data) {
    this.data = data;
    backPointer = data.len();
  }

  private boolean isEnd() {
    return frontPointer >= backPointer;
  }

  @Override
  public Optional<T> next() {
    if (isEnd()) {
      return Optional.empty();
    }

    var element = data.at(frontPointer);
    frontPointer += 1;
    return element;
  }

  @Override
  public java.util.Optional<T> nextBack() {
    if (isEnd()) {
      return Optional.empty();
    }

    backPointer -= 1;
    return data.at(backPointer);
  }
}
