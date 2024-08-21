package jarkz.collection.iterator.functools;

import jarkz.collection.iterator.Iterator;

@FunctionalInterface
public interface Collector<T, R> {
  public <I extends Iterator<T>> R collect(I iterator);
}
