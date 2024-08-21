package jarkz.collection.iterator;

import jarkz.collection.iterator.functools.Reverse;
import java.util.Optional;

public interface DoubleEndedIterator<T> extends Iterator<T> {
  public Optional<T> nextBack();

  @Override
  public default Reverse<T> reverse() {
    return new Reverse<>(this);
  }
}
