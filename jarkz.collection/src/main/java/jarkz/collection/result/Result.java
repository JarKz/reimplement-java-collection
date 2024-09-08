package jarkz.collection.result;

import jarkz.collection.maybe.Maybe;
import jarkz.collection.unit.Unit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.val;

/**
 * The class contains either Ok or Err value which represent by T and E generic types. At one time
 * class can contain only Ok or Err values, not both. Use it instead of throwing java-style
 * exceptions because it looks more pleasant and very readable.
 *
 * <p>> <b>Attention</b>: If you want to have Result which contains empty value when it is Ok or
 * Err, use {@link Unit}, because program will fail into {@link NullPointerException} if you'll pass
 * null-value.
 */
public class Result<T, E> {
  private T okValue;
  private E errValue;

  private Result() {}

  /** Creates a Result&lt;T, E&gt; with given value as Ok value. */
  public static <T, E> Result<T, E> Ok(@NonNull T value) {
    val result = new Result<T, E>();
    result.okValue = value;
    return result;
  }

  /** Creates a Result&lt;T, E&gt; with given value as Err value. */
  public static <T, E> Result<T, E> Err(@NonNull E error) {
    val result = new Result<T, E>();
    result.errValue = error;
    return result;
  }

  /**
   * Uses for checking Ok value if exists. Usually for logging or debugging, so DON'T use it for
   * changing Ok value! For this exists {@link Result#map}
   */
  public Result<T, E> inspect(@NonNull Consumer<T> consumer) {
    if (isOk()) {
      consumer.accept(okValue);
    }

    return this;
  }

  /**
   * Uses for checking Err value if exists. Usually for logging or debugging, so DON'T use it for
   * changing Err value! For this exists {@link Result#mapErr}
   */
  public Result<T, E> inspectErr(@NonNull Consumer<E> consumer) {
    if (isErr()) {
      consumer.accept(errValue);
    }

    return this;
  }

  /** Maps the Ok value to another by mapper function if present. Otherwise returns Err value. */
  public <R> Result<R, E> map(@NonNull Function<T, R> mapper) {
    if (isErr()) {
      return Err(errValue);
    }

    return Ok(mapper.apply(okValue));
  }

  /**
   * Maps the Ok value by mapper function and immediately returns if present, otherwise uses other
   * value.
   */
  public <R> R mapOr(@NonNull R other, @NonNull Function<T, R> mapper) {
    if (isOk()) {
      return throwIfNull(mapper.apply(okValue));
    }

    return other;
  }

  /**
   * Maps the Ok value by mapper function and immediately returns if present, otherwise uses default
   * value from {@link Function}.
   *
   * <p>> <b>Note</b>: Use it if the computation of default value is very expensive.
   */
  public <R> R mapOrElse(@NonNull Supplier<R> other, @NonNull Function<T, R> mapper) {
    if (isOk()) {
      return throwIfNull(mapper.apply(okValue));
    }

    return throwIfNull(other.get());
  }

  /** Maps the Err value by mapper function if exists. Otherwise returns Ok value. */
  public <R> Result<T, R> mapErr(@NonNull Function<E, R> mapper) {
    if (isErr()) {
      return Err(mapper.apply(errValue));
    }

    return Ok(okValue);
  }

  /** Uses other Result type if current doesn't have Ok value. */
  public <F> Result<T, F> or(@NonNull Result<T, F> other) {
    if (isOk()) {
      return Ok(okValue);
    }

    return other;
  }

  /**
   * Uses default Result type from {@link Supplier} if current doesn't have Ok value.
   *
   * <p>> <b>Note</b>: Use it if the computation of default value is very expensive.
   */
  public <F> Result<T, F> orElse(@NonNull Supplier<Result<T, F>> other) {
    if (isOk()) {
      return Ok(okValue);
    }

    return throwIfNull(other.get());
  }

  /** Returns current Err value if exists, otherwise returns other value. */
  public <R> Result<R, E> and(@NonNull Result<R, E> other) {
    if (isErr()) {
      return Err(errValue);
    }

    return other;
  }

  /**
   * Returns current Err value if exists, otherwise returns default Result type from {@link
   * Function} mapper for Ok value.
   */
  public <R> Result<R, E> andThen(@NonNull Function<T, Result<R, E>> function) {
    if (isErr()) {
      return Err(errValue);
    }

    return throwIfNull(function.apply(okValue));
  }

  /** Returns Ok value if exists, otherwise throws {@link IllegalStateException}. */
  public T unwrap() {
    if (isErr()) {
      throw new IllegalStateException("The value is err when unwraps");
    }

    return okValue;
  }

  /** Returns Err value if exists, otherwise throws {@link IllegalStateException}. */
  public E unwrapErr() {
    if (isOk()) {
      throw new IllegalStateException("The value is ok when unwraps err");
    }

    return errValue;
  }

  /** Returns Ok value if exists, otherwise returns other value. */
  public T unwrapOr(@NonNull T other) {
    if (isErr()) {
      return other;
    }

    return okValue;
  }

  /** Returns Ok value if exists, otherwise returns default value from {@link Supplier}. */
  public T unwrapOrElse(@NonNull Supplier<T> other) {
    if (isErr()) {
      return throwIfNull(other.get());
    }

    return okValue;
  }

  /** Returns Ok value without checking by nullability. */
  public T unwrapUnchecked() {
    return okValue;
  }

  /** Returns Err value whithout checking by nullability. */
  public E unwrapErrUnchecked() {
    return errValue;
  }

  /**
   * Returns Ok value if exists, otherwise throws {@link IllegalStateException} with a given
   * message.
   */
  public T expect(@NonNull String message) {
    if (isErr()) {
      throw new IllegalStateException(message);
    }

    return okValue;
  }

  /**
   * Returns Err value if exists, otherwise throws {@link IllegalStateException} with a given
   * message.
   */
  public E expectErr(@NonNull String message) {
    if (isOk()) {
      throw new IllegalStateException(message);
    }

    return errValue;
  }

  /** Converts to Maybe&lt;T&gt; if Ok value presents, otherwise returns empty Maybe&lt;T&gt;. */
  public Maybe<T> ok() {
    return Maybe.from(okValue);
  }

  /** Converts to Maybe&lt;E&gt; if Err value presents, otherwise returns empty Maybe&lt;E&gt;. */
  public Maybe<E> err() {
    return Maybe.from(errValue);
  }

  /** Execute code if the Ok value exists. */
  public void ifOk(@NonNull Consumer<T> consumer) {
    if (isOk()) {
      consumer.accept(okValue);
    }
  }

  /** Execute code if the Ok value exists and return the result of this code. */
  public <R> Maybe<R> ifOk(@NonNull Function<T, R> function) {
    if (isOk()) {
      return Maybe.Some(throwIfNull(function.apply(okValue)));
    }

    return Maybe.None();
  }

  /** Execute code if the Err value exists. */
  public void ifErr(@NonNull Consumer<E> consumer) {
    if (isErr()) {
      consumer.accept(errValue);
    }
  }

  /** Execute code if the Err value exists and return the result of this code. */
  public <R> Maybe<R> ifErr(@NonNull Function<E, R> function) {
    if (isErr()) {
      return Maybe.Some(throwIfNull(function.apply(errValue)));
    }

    return Maybe.None();
  }

  /** True, if current Result type holds the Ok value. */
  public boolean isOk() {
    return okValue != null;
  }

  /** True, if current Result type holds the Ok value and passes given {@link Predicate}. */
  public boolean isOkAnd(@NonNull Predicate<T> predicate) {
    return isOk() && predicate.test(okValue);
  }

  /** True, if current Result type holds the Err value. */
  public boolean isErr() {
    return errValue != null;
  }

  /** True, if current Result type holds the Err value and passes given {@link Predicate}. */
  public boolean isErrAnd(@NonNull Predicate<E> predicate) {
    return isErr() && predicate.test(errValue);
  }

  /**
   * Check the passed value by null-pointer and throws {@link NullPointerException} if it is null.
   */
  private <Value> Value throwIfNull(@NonNull Value value) {
    return value;
  }

  @Override
  public String toString() {
    if (isOk()) {
      return "Ok(" + okValue + ")";
    } else {
      return "Err(" + errValue + ")";
    }
  }
}
