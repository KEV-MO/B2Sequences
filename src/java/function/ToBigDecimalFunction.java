package function;

import java.math.BigDecimal;

/*
  Interface to define a mathematical function from an element of an integer
    sequence to a BigDecimal value.
*/
public interface ToBigDecimalFunction<T> extends Function<BigDecimal, T> {
  public BigDecimal applyAs(T value);
}
