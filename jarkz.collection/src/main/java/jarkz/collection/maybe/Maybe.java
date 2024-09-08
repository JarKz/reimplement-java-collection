package jarkz.collection.maybe;

import jarkz.collection.utils.function.Caller;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

/**
 * The class which can contain or not the value. Use it instead of {@link Optional} because it have
 * more helpful methods. Also the code readability with {@link Maybe} will be higher than {@link
 * Optional} because all naming for methods picked from Rust Programming Language and they are very
 * understandable.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Maybe<T> {

  private T value;

  /** Creates the Maybe&lt;T&gt; class with existing value. */
  public static <T> Maybe<T> Some(@NonNull T val) {
    return new Maybe<>(val);
  }

  /** Creates the Maybe&lt;T&gt; class with empty value. */
  public static <T> Maybe<T> None() {
    return new Maybe<>();
  }

  /**
   * Creates the Maybe&lt;T&gt; class with existing value when given value is non null, otherwise
   * creates Maybe&lt;T&gt; with empty value;
   */
  public static <T> Maybe<T> from(T val) {
    if (val == null) {
      return None();
    } else {
      return Some(val);
    }
  }

  /**
   * Uses for checking value. Usually for logging or debugging, so DON'T use it for changing inner
   * value! For this exists {@link Maybe#map}
   */
  public Maybe<T> inspect(@NonNull Consumer<T> inspector) {
    if (isSome()) {
      inspector.accept(value);
    }

    return this;
  }

  /**
   * Filters the value by predicate and returns the class Maybe&lt;T&gt; with existing value when
   * value is exists and passes {@link Predicate}, otherwise returns Maybe&lt;T&gt; with empty
   * value.
   */
  public Maybe<T> filter(@NonNull Predicate<T> predicate) {
    if (isSomeAnd(predicate)) {
      return this;
    }

    return None();
  }

  /** Maps the existing value by given function. */
  public <R> Maybe<R> map(@NonNull Function<T, R> mapper) {
    if (isNone()) {
      return None();
    }

    return Some(mapper.apply(value));
  }

  /**
   * Maps the existing value by given funcction and returns mapped value, otherwise returns the
   * other value.
   */
  public <R> R mapOr(@NonNull R other, @NonNull Function<T, R> mapper) {
    if (isSome()) {
      return throwIfNull(mapper.apply(value));
    }

    return other;
  }

  /**
   * Maps the existing value by given function and returns mapped value, otherwise calls the other
   * function which returns default value.
   *
   * <p>> <b>Note</b>: Use it if the computation of default value is very expensive.
   */
  public <R> R mapOrElse(@NonNull Supplier<R> other, @NonNull Function<T, R> mapper) {
    if (isSome()) {
      return throwIfNull(mapper.apply(value));
    }

    return throwIfNull(other.get());
  }

  /** Takes the value and leaves a slot with empty value if value exists. */
  public Maybe<T> take() {
    if (isNone()) {
      return None();
    }

    val result = Some(value);
    value = null;
    return result;
  }

  /**
   * Takes the value and leaves a slot with empty value if values exists and passes given {@link
   * Predicate}.
   */
  public Maybe<T> takeIf(@NonNull Predicate<T> predicate) {
    if (isNone() || predicate.negate().test(value)) {
      return None();
    }

    return take();
  }

  /** Replaces the current value by other and returns old value. */
  public Maybe<T> replace(@NonNull T other) {
    val prev = take();
    value = other;

    return prev;
  }

  /** Execute code if the value exists. */
  public void ifSome(@NonNull Consumer<T> consumer) {
    if (isSome()) {
      consumer.accept(value);
    }
  }

  /** Execute code if the value exists and return the result of this code. */
  public <R> Maybe<R> ifSome(@NonNull Function<T, R> function) {
    if (isNone()) {
      return None();
    }

    return Some(function.apply(value));
  }

  /** Execute code if the value is empty. */
  public void ifNone(@NonNull Caller caller) {
    if (isNone()) {
      caller.call();
    }
  }

  /** Execute code if the value is empty and return the result of this code. */
  public <R> Maybe<R> ifNone(@NonNull Supplier<R> supplier) {
    if (isNone()) {
      return Some(supplier.get());
    }

    return None();
  }

  /** Inserts a given value into a slot and retuns it as reference. */
  public T insert(@NonNull T insertingValue) {
    value = insertingValue;
    return value;
  }

  /** Returns the value if exists, otherwise insert other value and return it. */
  public T getOrInsert(@NonNull T other) {
    if (isNone()) {
      value = other;
    }

    return value;
  }

  /**
   * Returns the value if exists, otherwise insert default value from called {@link Supplier}.
   *
   * <p>> <b>Note</b>: Use it if the computation of default value is very expensive.
   */
  public T getOrInsertWith(@NonNull Supplier<T> supplier) {
    if (isNone()) {
      value = throwIfNull(supplier.get());
    }

    return value;
  }

  /** Uses other Maybe&lt;T&gt; if current value is empty, otherwise returns itself. */
  public Maybe<T> or(@NonNull Maybe<T> other) {
    if (isNone()) {
      return other;
    }

    return this;
  }

  /**
   * Uses default Maybe&lt;T&gt; value from {@link Supplier} if current value is empty, otherwise
   * returns itself.
   */
  public Maybe<T> orElse(@NonNull Supplier<Maybe<T>> other) {
    if (isNone()) {
      return throwIfNull(other.get());
    }

    return this;
  }

  /**
   * Returns other value if the current exists, otherwise returns Maybe&lt;R&gt; with empty value.
   */
  public <R> Maybe<R> and(@NonNull Maybe<R> other) {
    if (isNone()) {
      return None();
    }

    return other;
  }

  /**
   * Maps the value by mapper to Maybe<R> if exists and returns the result. Otherwise returns
   * Maybe&lt;T&gt; with empty value.
   *
   * <p>> <b>Note</b>: It is similar to {@code filterMap} and {@code flatMap} functions.
   */
  public <R> Maybe<R> andThen(@NonNull Function<T, Maybe<R>> other) {
    if (isNone()) {
      return None();
    }

    return throwIfNull(other.apply(value));
  }

  /** Returns the value if exists, otherwise throws {@link NullPointerException}. */
  public T unwrap() {
    if (isNone()) {
      throw new NullPointerException("The value is null when unwraps");
    }

    return value;
  }

  /** Returns the value if exists, otherwise use other value. */
  public T unwrapOr(@NonNull T other) {
    if (isNone()) {
      return other;
    }

    return value;
  }

  /**
   * Returns the value if exists, otherwise use default value from {@link Supplier}.
   *
   * <p>> <b>Note</b>: Use it if the computation of default value is very expensive.
   */
  public T unwrapOrElse(@NonNull Supplier<T> supplier) {
    if (isNone()) {
      return throwIfNull(supplier.get());
    }

    return value;
  }

  /**
   * Returns the values without checking existanse. Use this method if you're SURE that value is
   * exists. Otherwise a lot of places may be break with <b>null</b> value!
   */
  public T unwrapUnchecked() {
    return value;
  }

  /**
   * Returns the value if exists, otherwise throws {@link NullPointerException} with a given
   * message.
   */
  public T expect(@NonNull String msg) {
    if (isNone()) {
      throw new NullPointerException(msg);
    }

    return value;
  }

  /** True, if the value exists. */
  public boolean isSome() {
    return value != null;
  }

  /** True, if the values exists and passes {@link Predicate}. */
  public boolean isSomeAnd(@NonNull Predicate<T> predicate) {
    return value != null && predicate.test(value);
  }

  /** True, if the values is empty. */
  public boolean isNone() {
    return value == null;
  }

  /**
   * Check the passed value by null-pointer and throws {@link NullPointerException} if it is null.
   */
  private <Value> Value throwIfNull(@NonNull Value value) {
    return value;
  }

  @Override
  public String toString() {
    if (isSome()) {
      return "Some(" + value + ")";
    } else {
      return "None";
    }
  }
}
