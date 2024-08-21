package jarkz.collection.iterator;

public interface IntoIterator<T, I extends Iterator<T>> {
  public I intoIterator();
}
