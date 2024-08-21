# Reimplement Java Collection

As you know, Java does have bunch of useful collections like `Map<K, V`, `List<T>` and `Set<T>`. They're very good for most cases and I (jarkz) was curious what if I reimplement to my logic.

The logic must be not complex like `ArrayList<T>` in which a lot of constants, many complex computations, which gives us excellent result. I follow to simple logic with which in right hands they may be powerful.

## Motivation

I've tried and currently enjoying Rust as programming language, especially with `Iterator<T>` trait (i.e. interface), and I was curious can I implement it in Java code. To make it need to create a class which holds data - `Vector<T>`.

The results surprises, my implementation is much faster than Java Streaming and I decided to share code.

## Usage

Currently you can't use it either as library or as application. The only functionality which you can test - benchmarking. To run it:

```bash
./gradlew run
```

Or on Windows:

```powershell
./gradlew.bat run
```

The benchmarking may consume a lot of memory because uses $Integer.MAX_VALUE / 16 = 134217727$ values in single `Integer[]` array for all benches.
