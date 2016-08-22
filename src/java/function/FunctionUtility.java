package function;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;

public class FunctionUtility {
  public static HashMap<String, ToDoubleFunction<Integer>> getDefaultFunctionMap() {
    HashMap<String, ToDoubleFunction<Integer>> map = new HashMap<String, ToDoubleFunction<Integer>>();
    map.put("invert", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / i;
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }

      /*
      @Override
      public Double applyAs(Integer i) {
        return applyAsDouble(i);
      }
      */
    });

    map.put("default", map.get("invert"));

    map.put("invert^2", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / (i*i);
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }
    });

    map.put("golomb", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0;
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Math.floor((high - low) / inc);
      }
    });

    map.put("invert^(11/10)", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / Math.pow(i, 1.1);
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }
    });

    map.put("invert^(13/10)", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / Math.pow(i, 1.3);
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }
    });

    map.put("invert^3", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / Math.pow(i, 3);
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }
    });

    map.put("2^invert", new ToDoubleFunction<Integer>() {
      @Override
      public double applyAsDouble(Integer i) {
        return 1.0 / Math.pow(2, i);
      }

      @Override
      public double sum(Integer low, Integer high, Integer inc) {
        return Double.POSITIVE_INFINITY;
      }
    });

    return map;
  }

  public static ToBigDecimalFunction<Integer> getFunction(HashMap<String, ToBigDecimalFunction<Integer>> map, String functionName) {
    ToBigDecimalFunction<Integer> function = map.get(functionName);
    if (function == null) {
      if (map.containsKey("default")) {
        function = map.get("default");
      }
    }
    return function;
  }

  public static HashMap<String, ToBigDecimalFunction<Integer>> getBigDecimalFunctionMap() {
    HashMap<String, ToBigDecimalFunction<Integer>> map = new HashMap<String, ToBigDecimalFunction<Integer>>();
    map.put("invert", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(i), this.mc);
      }
    });

    map.put("default", map.get("invert"));

    map.put("invert^2", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(i*i), this.mc);
      }
    });

    map.put("golomb", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE;
      }
    });

    map.put("invert^(11/10)", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(Math.pow(i, 1.1)), this.mc);
      }
    });

    map.put("invert^(13/10)", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(Math.pow(i, 1.3)), this.mc);
      }
    });

    map.put("invert^3", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(Math.pow(i, 3)), this.mc);
      }
    });

    map.put("2^invert", new ToBigDecimalInteger(MathContext.DECIMAL128) {
      @Override
      public BigDecimal applyAs(Integer i) {
        return BigDecimal.ONE.divide(new BigDecimal(Math.pow(2, i)), this.mc);
      }
    });

    return map;
  }
}
