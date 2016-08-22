import verifier.SequenceVerifier;
import verifier.SidonVerifier;
import function.ToDoubleFunction;
import function.FunctionUtility;
import util.CommonUtility;
import util.FileUtility;
import util.SequenceUtility;

import java.io.FileInputStream;
import java.io.PrintStream;

import java.util.ArrayList;
// import java.util.function.ToDoubleFunction; ohhhhh no, not using java 8
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedMap;
import java.util.TreeMap;

// Use depth first search to find the B_2 sequence that optmizes the sum over a function

public class GreedySequence {
  protected SequenceVerifier<Integer> verifier;
  protected HashMap<String, ToDoubleFunction<Integer>> functionMap;

  /*
    Initializes the function HashMap.

    This map pairs String function names with implmentations of the
    ToDoubleFunction interface so that the function to maximize can be specified
    by name as a command line argument.

    Currently, the functions below are anonymous implmentations of "invert" : <code>(1 / n)</code>,
    "invert^2" : <code>(1 / n^2)</code>, and "golomb" : <code>(1)</code>
  */
  protected void initFunctionMap() {
    functionMap = FunctionUtility.getDefaultFunctionMap();
  }

  /*
    Returns a <code>ToDoubleFunction</code> object from the function map.

    @param functionName - Name of the function in the function map.
  */
  public ToDoubleFunction<Integer> getFunction(String functionName) {
    ToDoubleFunction<Integer> function;
    if (functionName == null || functionName.compareTo("") == 0 || !functionMap.containsKey(functionName)) {
      function = functionMap.get("default");
    } else {
      function = functionMap.get(functionName);
      if (function == null) {
        function = functionMap.get("default");
      }
    }
    return function;
  }

  /*
    Default constructor for OptimalSequenceSearch.

    Initializes the verifier and the function map.
  */
  public GreedySequence(SequenceVerifier<Integer> verifier) {
    this.verifier = verifier;
    initFunctionMap();
  }

  public ArrayList<Integer> greed(ArrayList<Integer> initialSequence, int start, int maximum, int maximumElements) {
    ArrayList<Integer> sequence = new ArrayList<Integer>(initialSequence);
    for (int i = start; i < maximum && sequence.size() < maximumElements; i++) {
      if (verifier.verifyElement(sequence, i)) {
        sequence.add(i);
      }
    }
    return sequence;
  }

  /*
    Class to hold the parameters parsed from the command line arguments.
  */
  public static class Parameters {
    public int elementCount;
    public int maximum;
    public int maximumElements;
    public int start;
    public String functionName;
    public String sequenceFile;

    public Parameters() {
      maximum = Integer.MAX_VALUE;
      maximumElements = Integer.MAX_VALUE;
      start = 1;
      functionName = "default";
      sequenceFile = null;
      elementCount = -1;
    }
  }

  /*
    Parse the command line arguments.
  */
  public static GreedySequence.Parameters parseArgs(String [] args) {
    GreedySequence.Parameters parameters = new GreedySequence.Parameters();
    Pattern max = Pattern.compile("(-m|--maximum)=(?<maximum>[0-9]+)");
    Pattern maximumElements = Pattern.compile("(-e|--maximumElements)=(?<maximumElements>[0-9]+)");
    Pattern start = Pattern.compile("(-i|--start)=(?<start>[0-9]+)");
    Pattern function = Pattern.compile("(-f|--function)=(?<function>.*)");
    Pattern sequence = Pattern.compile("(-s|--sequence)");
    Pattern elementCount = Pattern.compile("(-e|--initialElements)=(?<elements>[0-9]+)");
    Matcher matcher;
    String arg;

    for (int i = 0; i < args.length; i++) {
      arg = args[i];

      matcher = max.matcher(arg);
      if (matcher.matches()) {
        parameters.maximum = Integer.parseInt(matcher.group("maximum"));
        continue;
      }

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

      matcher = elementCount.matcher(arg);
      if (matcher.matches()) {
        parameters.elementCount = Integer.parseInt(matcher.group("elements"));
        continue;
      }

      matcher = maximumElements.matcher(arg);
      if (matcher.matches()) {
        parameters.maximumElements = Integer.parseInt(matcher.group("maximumElements"));
        continue;
      }

      matcher = start.matcher(arg);
      if (matcher.matches()) {
        parameters.start = Integer.parseInt(matcher.group("start"));
        continue;
      }
    }

    return parameters;
  }

  protected static Map<String, String> getParameters(Double weight, Integer elementCount) {
    HashMap<String, String> parameterMap = new HashMap<String, String>();
    if (weight != null) {
      parameterMap.put("weight", weight.toString());
    }

    if (elementCount != null) {
      parameterMap.put("elements", elementCount.toString());
    }

    return parameterMap;
  }

  /*
    <code>GreedySequence</code> entry point.
  */
  public static void main(String [] args) {

    GreedySequence.Parameters parameters = GreedySequence.parseArgs(args);
    ArrayList<Integer> initialSequence = CommonUtility.getInitialSequence(parameters.sequenceFile, parameters.elementCount);
    ArrayList<Integer> greedySequence;

    GreedySequence gs = new GreedySequence(new SidonVerifier());
    greedySequence = gs.greed(initialSequence, parameters.start, parameters.maximum, parameters.maximumElements);

    Map<String, String> parameterMap = GreedySequence.getParameters(SequenceUtility.reverseSum(greedySequence, gs.getFunction(parameters.functionName)), greedySequence.size());
    SequenceUtility.printSequence(System.out, parameterMap, greedySequence);
  }
}
