package function;

/*
  Interface to define a mathematical function from an element of an integer
    sequence to a double value.
*/
public abstract class ToDoubleFunction<T> implements Function<Double, T> {
  public Double applyAs(T value) {
    return applyAsDouble(value);
  }

  public abstract double applyAsDouble(T value);

//  public Double sum(T low, T high, T inc);
  // Returns an approximate upper bound on the inclusive sum of the terms of the sequence from <code>low</code> to <code>high</code> when incrementing by <code>inc</code>.
  public abstract double sum(T low, T high, T inc);
}
