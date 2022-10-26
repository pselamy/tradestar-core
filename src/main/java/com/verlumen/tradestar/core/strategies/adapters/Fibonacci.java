package com.verlumen.tradestar.core.strategies.adapters;

import com.google.common.collect.ImmutableSet;

import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Fibonacci {
  private static final double SQUARE_ROOT_OF_5 = sqrt(5);
  private static final double PHI = (1 + SQUARE_ROOT_OF_5) / 2;

  private static int nthFibonacciTerm(int n) {
    return (int) ((pow(PHI, n) - pow(-PHI, -n)) / SQUARE_ROOT_OF_5);
  }

  static ImmutableSet<Integer> fibonacciRange(int fromInclusive, int toInclusive) {
    return IntStream.rangeClosed(fromInclusive, toInclusive)
        .map(Fibonacci::nthFibonacciTerm)
        .boxed()
        .collect(toImmutableSet());
  }
}
