package jarkz.collection.vector;

import jarkz.collection.iterator.DoubleEndedIterator;
import jarkz.collection.iterator.IntoIterator;
import jarkz.collection.iterator.Iterator;
import java.util.Arrays;
import java.util.Optional;

public class Vector<T> implements IntoIterator<T, DoubleEndedIterator<T>> {
  private T[] data;
  private int len;

  @SuppressWarnings("unchecked")
  public Vector() {
    // SAFETY: the created array always is zero-sized
    // so we don't care about unchecked type casting
    data = (T[]) new Object[0];
    len = 0;
  }

  @SafeVarargs
  public Vector(T... data) {
    for (T element : data) {
      if (element == null) {
        throw new IllegalArgumentException("Found a nullable value in given collection!");
      }
    }

    this.data = data;
    len = data.length;
  }

  public int len() {
    return len;
  }

  public boolean isEmpty() {
    return len == 0;
  }

  public Optional<T> pop() {
    if (isEmpty()) {
      return Optional.empty();
    }

    len -= 1;
    var element = data[len];
    data[len] = null;

    return Optional.of(element);
  }

  public void push(T element) {
    if (data.length == len) {
      var newLen = (len + 1) * 2;
      data = Arrays.copyOf(data, newLen);
    }

    data[len] = element;
    len += 1;
  }

  public Optional<T> at(int index) {
    if (index < 0 || len <= index) {
      return Optional.empty();
    }

    return Optional.of(data[index]);
  }

  @Override
  public DoubleEndedIterator<T> intoIterator() {
    return new IterVector<>(this);
  }

  public static <T, I extends Iterator<T>> Vector<T> collect(I iterator) {
    return iterator.fold(
        new Vector<>(),
        (acc, element) -> {
          acc.push(element);
          return acc;
        });
  }

  public <I extends Iterator<T>> void extend(IntoIterator<T, I> iterable) {
    extend(iterable.intoIterator());
  }

  public <I extends Iterator<T>> void extend(I iterator) {
    iterator.forEach(element -> push(element));
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Vector[");
    for (int i = 0; i < len; i++) {
      builder.append(data[i]);

      if (i < len - 1) {
        builder.append(", ");
      }
    }

    builder.append("]");
    return builder.toString();
  }
}
