package util;

import function.ToDoubleFunction;
import function.ToBigDecimalFunction;
import function.FunctionUtility;

import java.io.PrintStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class SequenceUtility {
  protected static final int PARAMETER_NAME = 1;
  protected static final int PARAMETER_VALUE = 2;
  protected static final String DEFAULT_HEAD = "{";
  protected static final String DEFAULT_TAIL = "}";
  protected static final String DEFAULT_SEPARATOR = ", ";
  protected static final MathContext DEFAULT_CONTEXT = MathContext.DECIMAL128;
  protected static Map<String, Utility> utilityMap = getUtilityMap();
  protected static Map<String, MathContext> contextMap = getContextMap();

  public static enum ParameterMap {
    WEIGHT (0),
    ELEMENTS (1);

    protected int index;

    ParameterMap(int index) {
      this.index = index;
    }

    public int getIndex() {
      return index;
    }
  }

  public static final String PARAMETER_NAMES[] = {"weight", "elements"};

  protected static HashMap<String, Utility> getUtilityMap() {
    HashMap<String, Utility> map = new HashMap<String, Utility>();
    map.put("h", Utility.HELP);
    map.put("help", Utility.HELP);
    map.put("s", Utility.SUM);
    map.put("sum", Utility.SUM);
    map.put("r", Utility.REFLECTION);
    map.put("reflection", Utility.REFLECTION);
    map.put("d", Utility.DIFFERENCE_TABLE);
    map.put("difference-table", Utility.DIFFERENCE_TABLE);
    map.put("u", Utility.SUM_TABLE);
    map.put("sum-table", Utility.SUM_TABLE);
    return map;
  }

  protected static HashMap<String, MathContext> getContextMap() {
    HashMap<String, MathContext> map = new HashMap<>();
    map.put("D128", MathContext.DECIMAL128);
    map.put("DECIMAL128", MathContext.DECIMAL128);
    map.put("D64", MathContext.DECIMAL64);
    map.put("DECIMAL64", MathContext.DECIMAL64);
    map.put("D32", MathContext.DECIMAL32);
    map.put("DECIMAL32", MathContext.DECIMAL32);
    map.put("UNL", MathContext.UNLIMITED);
    map.put("UNLIMITED", MathContext.UNLIMITED);
    return map;
  }

  /*
    Simple print function, prints <code>sequence</code> to <code>out</code>.
  */
  public static void print(
      PrintStream out,
      ArrayList<Integer> sequence) {
    print(out, sequence, 0, sequence.size());
  }

  public static void print(
      PrintStream out,
      ArrayList<Integer> sequence,
      boolean newlines) {
    if (newlines) {
      print(out, sequence, 0, sequence.size(), "", "\n", "");
    } else {
      print(out, sequence, 0, sequence.size());
    }
  }

  /*
    Prints elements of <code>sequence</code> from the index <code>first</code> to <code>last - 1</code>
      to <code>out</code>.
  */
  public static void print(
      PrintStream out,
      ArrayList<Integer> sequence,
      int first,
      int last) {
    print(out, sequence, 0, sequence.size(), DEFAULT_HEAD, DEFAULT_SEPARATOR, DEFAULT_TAIL);
  }

  /*
  Prints elements of <code>sequence</code> from the index <code>first</code> to <code>last - 1</code>
    to <code>out</code>.

  Allows user to specify a leading string, a trailing string, and the separator
    between elements.
  */
  public static void print(
      PrintStream out,
      ArrayList<Integer> sequence,
      int first,
      int last,
      String head,
      String separator,
      String tail) {
    out.print(head);
	if (first >= 0 && first < sequence.size()) {
		out.print(sequence.get(first));
	}
    for (int i = first + 1; i < last; i++) {
      out.print(separator);
      out.print(sequence.get(i));
    }
    out.print(tail);
  }

  /*
    Gets the element count from a sequence.
  */
  protected static int getIntegerParameter(Map<String, String> parameters, String parameterName, int defaultInt) {
    int ret = defaultInt;
    String parameterString = parameters.get(parameterName);
    if (parameterString != null) {
      try {
        ret = Integer.parseInt(parameterString);
      } catch (NumberFormatException nfe) {
        ret = defaultInt;
      }
    }
		return ret;
  }

	protected static void writeParameter(PrintStream out, String parameterName, String parameterValue) {
		out.println("--" + parameterName + "={" + parameterValue + "}");
	}

	protected static Map<String, String> getParameters(Scanner scan, Map<String, String> initialMap) {
		Map<String, String> ret;
		Pattern parameter = Pattern.compile("--(?<parameterName>.+)=\\{(?<parameter>.+)\\}");
		MatchResult mr = null;

		if (initialMap == null) {
			ret = new HashMap<String, String>();
		} else {
			ret = initialMap;
		}

		if (scan.findInLine(parameter) != null) {
			mr = scan.match();
		}

		while (mr != null) {
			if (mr instanceof Matcher) {
				Matcher m = (Matcher)mr;
				ret.put(m.group("parameterName"), m.group("parameter"));
			} else {
				ret.put(mr.group(PARAMETER_NAME), mr.group(PARAMETER_VALUE));
			}

			mr = null;
			if (scan.findInLine(parameter) != null) {
				mr = scan.match();
			}
		}

		return ret;
	}

  /*
    Gets an <code>ArrayList</code> of integers from <code>scan</code>.

    @param scan - Scanner to load elements from.
    @param elementCount - Maximum number of elements to load. If negative,
      elements are loaded until <code>scan</code> no longer has <code>int</code> tokens.
  */
  protected static ArrayList<Integer> loadElements(Scanner scan, int elementCount, int skipCount) {
    ArrayList<Integer> sequence;
    if (elementCount > 0) {
      sequence = new ArrayList<>(elementCount);
    } else {
      sequence = new ArrayList<>();
    }

    while (scan.hasNextInt() && skipCount-- != 0) {
      scan.nextInt();
    }

    while (scan.hasNextInt() && elementCount-- != 0) {
      sequence.add(scan.nextInt());
    }

    return sequence;
  }

  /*
    Loads a sequence of integers from <code>in</code>.
  */
  public static ArrayList<Integer> load(InputStream in) {
    Scanner scan = new Scanner(in);
    Map<String, String> parameters = getParameters(scan, null);
    int elements = getIntegerParameter(parameters, "elements", -1);

    return loadElements(scan, elements, 0);
  }

  /*
    Loads a sequence of integer from <code>in</code> ignoring the element argument
      in the stream.

    @param in - <code>InputStream</code> from which the sequence is loaded.
    @param elementCountOverride - Maximum number of element to load. Overrides
      the maximum element indicator in the stream.
  */
  public static ArrayList<Integer> load(InputStream in, int elementCountOverride) {
    Scanner scan = new Scanner(in);
    Map<String, String> parameters = getParameters(scan, null);
    int elements = elementCountOverride;

    return loadElements(scan, elements, 0);
  }

  public static ArrayList<Integer> load(InputStream in, int elementCountOverride, int skipCount) {
    Scanner scan = new Scanner(in);
    Map<String, String> parameters = getParameters(scan, null);
    int elements = elementCountOverride;
    return loadElements(scan, elements, skipCount);
  }

  /*
    Computes the sum of a sequence of doubles from the sequence of integers.

    @param sequence - Sequence of integers from which the sum is computed.
    @param function - Mapping function from the element of the sequence to the
      corresponding weight to be added to the sum.
  */
  public static double computeSum(ArrayList<Integer> sequence, ToDoubleFunction<Integer> function) {
    double sum = 0.0;
    for (Integer i : sequence) {
      sum += function.applyAsDouble(i);
    }
    return sum;
  }

  public static double reverseSum(ArrayList<Integer> sequence, ToDoubleFunction<Integer> function) {
    double sum = 0.0;
    for (int i = sequence.size() - 1; i >= 0; i--) {
      sum += function.applyAsDouble(sequence.get(i));
    }
    return sum;
  }

  public static BigDecimal computeSum(ArrayList<Integer> sequence, ToBigDecimalFunction<Integer> function, MathContext context) {
    BigDecimal sum = new BigDecimal(0.0);
    for (Integer i : sequence) {
      sum = sum.add(function.applyAs(i), context);
    }
    return sum;
  }

  public static BigDecimal reverseSum(ArrayList<Integer> sequence, ToBigDecimalFunction<Integer> function, MathContext context) {
    BigDecimal sum = new BigDecimal(0.0);
    for (int i = sequence.size() - 1; i >= 0; i--) {
      sum = sum.add(function.applyAs(i), context);
    }
    return sum;
  }

  public static enum Utility {
    SUM,
    REFLECTION,
    DIFFERENCE_TABLE,
    SUM_TABLE,
    HELP
  }

  public static enum Print {
    SINGLE,
    MULTI,
    NO
  }

  /*
    Class to hold the parameters parsed from the command line arguments.
  */
  public static class Parameters {
    public int firstElement;
    public int lastElement;
    public Utility utility;
    public String functionName;
    public String sequenceFile;
    public Print printSequence;
    public MathContext context;
    public HashMap<String, String> specific;

    public Parameters() {
      firstElement = 0;
      lastElement = Integer.MAX_VALUE;
      utility = Utility.SUM;
      functionName = "default";
      sequenceFile = null;
      printSequence = Print.NO;
      context = SequenceUtility.DEFAULT_CONTEXT;
      specific = new HashMap<>();
    }
  }

  protected static void printParameters(
      PrintStream out,
      Map<String, String> parameters) {
    for (Map.Entry<String, String> pair : parameters.entrySet()) {
      writeParameter(out, pair.getKey(), pair.getValue());
    }
  }

  /*
    Utility function to print a sequence's rank in the search, its total weight,
      and the sequence itself.
  */
  public static void printSequence(
      PrintStream out,
      Map<String, String> parameters,
      ArrayList<Integer> sequence) {
    printParameters(out, parameters);
    print(out, sequence, 0, sequence.size(), "", "\n", "");
    out.println();
  }

  /*
    Parse the command line arguments.
  */
  public static SequenceUtility.Parameters parseArgs(String [] args) {
    SequenceUtility.Parameters parameters = new SequenceUtility.Parameters();
    /*
    Pattern sumTable = Pattern.compile("(-t|--sumTable)");
    Pattern differenceTable = Pattern.compile("(-d|--differenceTable)");
    Pattern reflection = Pattern.compile("(-r|--reflection)");
    Pattern sum = Pattern.compile("(-u|--sum)");
    */
    Pattern flag = Pattern.compile("-((?<letter>\\p{Alpha})|-(?<name>\\p{Alpha}+))");
    Pattern parameter = Pattern.compile("-((?<letter>\\p{Alpha})|-(?<name>\\p{Alpha}+))=\\{(?<parameter>.+)\\}");
    Pattern function = Pattern.compile("-(f|-function)=(?<function>.+)");
    Pattern sequence = Pattern.compile("-(s|-sequence)");
    Pattern first = Pattern.compile("-(i|-first)=(?<first>[0-9]+)");
    Pattern last = Pattern.compile("-(l|-last)=(?<last>[0-9]+)");
    Pattern printSequence = Pattern.compile("-((?<mulitline>p|-print)|(?<singleline>q|-1print))");
    Pattern context = Pattern.compile("-(c|-context)=(?<context>.+)");
    Pattern utility = Pattern.compile("-(u|utility)=(?<utility>.+)");
    HashMap<String, Utility> utilityMap = SequenceUtility.getUtilityMap();
    Matcher matcher;
    String param;

    String arg;

    for (int i = 0; i < args.length; i++) {
      arg = args[i];

      matcher = function.matcher(arg);
      if (matcher.matches()) {
        parameters.functionName = matcher.group("function");
        continue;
      }

      matcher = sequence.matcher(arg);
      if (matcher.matches()) {
        parameters.sequenceFile = args[++i];
        continue;
      }

      matcher = first.matcher(arg);
      if (matcher.matches()) {
        parameters.firstElement = Integer.parseInt(matcher.group("first"));
        continue;
      }

      matcher = last.matcher(arg);
      if (matcher.matches()) {
        parameters.lastElement = Integer.parseInt(matcher.group("last"));
        continue;
      }

      matcher = printSequence.matcher(arg);
      if (matcher.matches()) {
        if (matcher.group("multiline") != null) {
          parameters.printSequence = Print.MULTI;
        } else if (matcher.group("singleline") != null) {
          parameters.printSequence = Print.SINGLE;
        } else {
          parameters.printSequence = Print.NO;
        }
        continue;
      }

      matcher = context.matcher(arg);
      if (matcher.matches()) {
        parameters.context = SequenceUtility.contextMap.get(matcher.group("context"));
        if (parameters.context == null) {
          parameters.context = SequenceUtility.DEFAULT_CONTEXT;
        }
        continue;
      }

      matcher = utility.matcher(arg);
      if (matcher.matches()) {
        parameters.utility = utilityMap.get(matcher.group("utility"));
        if (parameters.utility == null) {
          parameters.utility = SequenceUtility.Utility.SUM;
        }
        continue;
      }

      matcher = parameter.matcher(arg);
      if (matcher.matches()) {
        param = matcher.group("letter");
        if (param == null) {
          param = matcher.group("name");
        }
        parameters.specific.put(param, matcher.group("parameter"));
        continue;
      }

      matcher = flag.matcher(arg);
      if (matcher.matches()) {
        param = matcher.group("letter");
        if (param == null) {
          param = matcher.group("name");
        }
        parameters.specific.put(param, "");
        continue;
      }
    }

    return parameters;
  }

  public static void main(String [] args) {
    SequenceUtility.Parameters parameters = SequenceUtility.parseArgs(args);
    ArrayList<Integer> sequence = CommonUtility.getSequence(parameters.sequenceFile, parameters.firstElement, parameters.lastElement);
    SequenceUtility.print(System.out, sequence);
    System.out.println();
    HashMap<String, String> output = new HashMap<>();

    HashMap<String, ToBigDecimalFunction<Integer>> functionMap = FunctionUtility.getBigDecimalFunctionMap();
    ToBigDecimalFunction<Integer> function = FunctionUtility.getFunction(functionMap, parameters.functionName);

    switch (parameters.utility) {
      case SUM:
        BigDecimal sum = SequenceUtility.computeSum(sequence, function, parameters.context);
        output.put("weight", sum.toString());
        SequenceUtility.printParameters(System.out, output);
      break;
      case REFLECTION:
        String parameter = parameters.specific.get("axis");
        int axis = (parameter == null) ? sequence.get(sequence.size() - 1) : Integer.parseInt(parameter);
        ArrayList<Integer> newSequence = new ArrayList<>(sequence.size());
        for (Integer i : sequence) {
          newSequence.add(axis - i);
        }
        SequenceUtility.print(System.out, newSequence, true);
      break;
      case DIFFERENCE_TABLE:
        // getDifferenceTable();
        // printDifferenceTable();
      break;
      case SUM_TABLE:
        // getSumTable();
        // printSumTable();
      break;
      case HELP:
        // printHelp();
      break;
    }

    switch (parameters.printSequence) {
      case MULTI:
        SequenceUtility.print(System.out, sequence, true);
      break;
      case SINGLE:
        SequenceUtility.print(System.out, sequence, false);
      break;
      case NO:
      break;
    }
  }
}
