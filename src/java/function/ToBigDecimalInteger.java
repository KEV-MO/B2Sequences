package function;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class ToBigDecimalInteger implements ToBigDecimalFunction<Integer> {
  protected MathContext mc;

  public ToBigDecimalInteger(MathContext mc) {
    this.mc = mc;
  }

  public abstract BigDecimal applyAs(Integer value);
}
