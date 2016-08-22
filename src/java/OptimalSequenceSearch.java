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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedMap;
import java.util.TreeMap;

// Use depth first search to find the B_2 sequence that optmizes the sum over a function

public class OptimalSequenceSearch {
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
    Default constructor for OptimalSequenceSearch.

    Initializes the verifier and the function map.
  */
  public OptimalSequenceSearch(SequenceVerifier<Integer> verifier) {
    if (verifier != null) {
      this.verifier = verifier;
    } else {
      this.verifier = new SidonVerifier();
    }
    initFunctionMap();
  }

  /*
    Attempts to add <code>sequence</code> to the SortedMap <code>bestSequences</code>.

    If <code>bestSequences</code> already has <code>nBestSequences</code> and <code>totalWeight</code>
      isn't better than the total weight of any sequence in <code>bestSequences</code>
      then <code>sequence</code> is not added to the SortedMap.

    @param bestSequences - SortedMap storing the total weight of the
      <code>nBestSequences</code> sequences as a key to the sequence itself.
    @param totalWeight - Key to attempt to add to <code>bestSequences</code>
    @param sequence - The sequence corresponding to <code>totalWeight</code> to be added
      to <code>bestSequences</code>.
    @param nBestSequences - The maximum number of sequences that <code>bestSequences</code>
      should store.
  */
  protected static void addSequence(
      SortedMap<Double, ArrayList<Integer>> bestSequences,
      double totalWeight,
      ArrayList<Integer> sequence,
      int nBestSequences) {
    if (bestSequences.size() < nBestSequences) {
      bestSequences.put(totalWeight, sequence);
    } else if (totalWeight > bestSequences.firstKey()) {
      bestSequences.put(totalWeight, sequence);
      bestSequences.remove(bestSequences.firstKey());
    }
  }

  /*
    Recursive depth first search of every sequence beginning with the sub-sequence
      <code>sequence<code> that gets produced by the verifier of this
      <code>OptimalSequenceSearch</code> object.

    @param totalWeight - Total weight of <code>sequence</code>.
    @param sequence - Previously verified by parent search to append to.
    @param bestSequences - SortedMap storing the best sequences found during the search.
    @param depth - Current integer to evaluate.
    @param maximum - Maximum integer to evaluate.
    @param nBestSequences - Maximum number of sequences to store in <code>bestSequences</code>
  */
  protected void subsearch(
      double totalWeight,
      ArrayList<Integer> sequence,
      SortedMap<Double, ArrayList<Integer>> bestSequences,
      int depth,
      int maximum,
      int maxElements,
      int nBestSequences,
      ToDoubleFunction<Integer> function) {
    if (depth < maximum && sequence.size() < maxElements) {
      if (verifier.verifyElement(sequence, depth)) {
        ArrayList<Integer> next = new ArrayList<>(sequence);
        next.add(depth);
        subsearch(totalWeight + function.applyAsDouble(depth), next, bestSequences, depth + 1, maximum, maxElements, nBestSequences, function);
      }
	     subsearch(totalWeight, sequence, bestSequences, depth + 1, maximum, maxElements, nBestSequences, function);
    } else if (depth == maximum || sequence.size() == maxElements) {
		  if (verifier.verifyElement(sequence, depth)) {
			   sequence.add(depth);
			   totalWeight += function.applyAsDouble(depth);
		  }
		  addSequence(bestSequences, totalWeight, sequence, nBestSequences);
	  }
  }

  /*
    Returns a <code>ToDoubleFunction</code> object from the function map.

    @param functionName - Name of the function in the function map.
  */
  protected ToDoubleFunction<Integer> getFunction(String functionName) {
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
    Depth first search of sequences to depth <code>maximum<\code>.

    @param initialSequence - Initial terms of the sequence.
    @param maximum - Maximum depth to search to.
    @param nBestSequences - Maximum number of sequences to return.
    @param functionName - Name of the weight function to calulate a sum across
      the sequence.
  */
  public SortedMap<Double, ArrayList<Integer>> search(
      ArrayList<Integer> initialSequence,
      int maximum,
      int maxElements,
      int nBestSequences,
      String functionName) {
    SortedMap<Double, ArrayList<Integer>> bestSequences = new TreeMap<Double, ArrayList<Integer>>();
    ToDoubleFunction<Integer> function = getFunction(functionName);
    int initialDepth = (initialSequence.size() > 0 ? initialSequence.get(initialSequence.size() - 1) + 1 : 1);

    subsearch(SequenceUtility.computeSum(initialSequence, function), initialSequence, bestSequences, initialDepth, maximum, maxElements, nBestSequences, function);
    return bestSequences;
  }

  /*
    Alternative search methode using a threshold instead.

    Not implemented.
  */
  public SortedMap<Double, ArrayList<Integer>> search(int maximum, double threshold) {
    return null;
  }


  /*
    Class to hold the parameters parsed from the command line arguments.
  */
  public static class Parameters {
    public double threshold;
    public int elementCount;
    public int maximum;
    public int maxElement;
    public int nBestSequences;
    public String functionName;
    public String sequenceFile;

    public Parameters() {
      maximum = Integer.MAX_VALUE;
      maxElement = Integer.MAX_VALUE;
      nBestSequences = Integer.MIN_VALUE;
      threshold = Double.NEGATIVE_INFINITY;
      functionName = "default";
      sequenceFile = null;
      elementCount = -1;
    }
  }

  /*
    Utility function to print a sequence's rank in the search, its total weight,
      and the sequence itself.
  */
  protected static void printSequence(
      PrintStream out,
      int rank,
      double totalWeight,
      ArrayList<Integer> sequence) {
    out.print(rank);
    out.print(" | ");
    out.print(totalWeight);
    out.print(" : ");
    SequenceUtility.print(out, sequence);
    out.println("");
  }

  /*
    Prints all of the sequences in the SortedMap <code>bestSequences</code>.
  */
  public static void printBestSequences(SortedMap<Double, ArrayList<Integer>> bestSequences) {
    int rank = 1;
    double key;
    while (bestSequences.size() > 0) {
      key = bestSequences.lastKey();
      OptimalSequenceSearch.printSequence(System.out, rank, key, bestSequences.get(key));
      bestSequences = bestSequences.headMap(key);
      rank++;
    }
  }

  /*
    Parse the command line arguments.
  */
  public static OptimalSequenceSearch.Parameters parseArgs(String [] args) {
    OptimalSequenceSearch.Parameters parameters = new OptimalSequenceSearch.Parameters();
    Pattern max = Pattern.compile("(-m|--maximum)=(?<maximum>[0-9]+)");
    Pattern maximumElements = Pattern.compile("(-x|--maximumElements)=(?<maximumElements>[0-9]+)");
    Pattern bestSequences = Pattern.compile("(-b|--bestcount)=(?<bestSequences>[0-9]+)");
    Pattern thresholdPattern = Pattern.compile("(-t|--threshold)=(?<threshold>[0-9]*\\.[0-9]+)");
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

	  matcher = maximumElements.matcher(arg);
	  if (matcher.matches()) {
	  	parameters.maxElement = Integer.parseInt(matcher.group("maximumElements"));
		continue;
	  }

      matcher = bestSequences.matcher(arg);
      if (matcher.matches()) {
        parameters.nBestSequences = Integer.parseInt(matcher.group("bestSequences"));
        continue;
      }

      matcher = thresholdPattern.matcher(arg);
      if (matcher.matches()) {
        parameters.threshold = Double.parseDouble(matcher.group("threshold"));
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
    }

    return parameters;
  }

  /*
    <code>OptimalSequenceSearch</code> entry point.
  */
  public static void main(String [] args) {

    OptimalSequenceSearch.Parameters parameters = OptimalSequenceSearch.parseArgs(args);
    ArrayList<Integer> initialSequence = CommonUtility.getInitialSequence(parameters.sequenceFile, parameters.elementCount);
    SortedMap<Double, ArrayList<Integer>> bestB2Sequences = new TreeMap<>();

    if (parameters.nBestSequences == Integer.MIN_VALUE
        && parameters.threshold == Double.NEGATIVE_INFINITY) {
      parameters.nBestSequences = 1;
    }

    OptimalSequenceSearch oss = new OptimalSequenceSearch(new SidonVerifier());
    if (parameters.nBestSequences == Integer.MIN_VALUE) {
      // bestB2Sequences = oss.search(parameters.maximum, parameters.threshold, parameters.functionName);
      // Not implemented yet.
    } else {
      SequenceUtility.print(System.out, initialSequence);
	  System.out.println();
      bestB2Sequences = oss.search(initialSequence, parameters.maximum, parameters.maxElement, parameters.nBestSequences, parameters.functionName);
    }

    OptimalSequenceSearch.printBestSequences(bestB2Sequences);
  }
}
