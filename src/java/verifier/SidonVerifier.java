package verifier;

import util.Search;
import java.util.AbstractList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/*
  Verifier for Sidon/B_2 sequences.
*/
public class SidonVerifier extends AbstractSequenceVerifier<Integer> {
  public Integer upperBound(int index) {
    return Integer.MAX_VALUE;
  }

  public Integer lowerBound(int index) {
    // lower bound ???
    return ((index * (index + 1)) >> 1) + 1;
  }

  public boolean verifyElement(AbstractList<Integer> sequence, Integer element) {
    int sum;
    int query;
    boolean ret = true;
    for (int i = sequence.size() - 1; i >= 0 && ret; i--) {
      if ((sequence.get(i) << 1) < element) { // Two's complement left bitshift is equivalent to multiplying by 2.
        ret = true;
        break;
      }
      for (int j = i; j >= 0; j--) {
        sum = sequence.get(i) + sequence.get(j);
        query = sum - element;
        if (query < 0) {
          break;
        }
        if (Search.binarySearch(sequence, query, 0, j)) {
          ret = false;
          break;
        }
      }
    }
    return ret;
  }

  /*
    Faster verification if a <code>TreeSet</code> is used.
  */
  public boolean treeVerifyElement(AbstractList<Integer> sequence, TreeSet<Integer> set, Integer element) {
    int query;
    boolean ret = true;
    for (int i = 0; i < sequence.size(); i++) {
      query = element + sequence.get(i);
      if (query > set.last()) { // This sum is larger than every element in the pariwise sum set and subsequence sums will be as well.
        break;
      }
      if (set.contains(query)) {
        ret = false;
        break;
      }
    }
    return ret;
  }

  public boolean verifyElement(AbstractList<Integer> sequence, Set<Integer> set, Integer element) {
    boolean ret = true;
    if (set instanceof TreeSet) {
      ret = treeVerifyElement(sequence, (TreeSet<Integer>) set, element);
    } else {
      int query = 0;
      for (int i = 0; i < sequence.size(); i++) {
        query = element + sequence.get(i);
        if (set.contains(query)) {
          ret = false;
          break;
        }
      }
    }
    return ret;
  }

  /*
    Faster verification if a <code>TreeMap</code> is used.
  */
  public boolean treeVerifyElement(AbstractList<Integer> sequence, TreeMap <Integer, Integer> map, Integer element) {
    int query;
    boolean ret = true;
    for (int i = 0; i < sequence.size(); i++) {
      query = element + sequence.get(i);
      if (query > map.lastKey()) { // This sum is larger than every element in the pairwise sum set and subsequence sums will be as well.
        break;
      }
      if (map.containsKey(query)) {
        ret = false;
        break;
      }
    }
    return ret;
  }

  public boolean verifyElement(AbstractList<Integer> sequence, Map<Integer, Integer> map, Integer element) {
    boolean ret = true;
    if (map instanceof TreeMap) {
      ret = treeVerifyElement(sequence, (TreeMap<Integer, Integer>) map, element);
    } else {
      int query;
      for (int i = 0; i < sequence.size(); i++) {
        query = element + sequence.get(i);
        if (map.containsKey(query)) {
          ret = false;
          break;
        }
      }
    }
    return ret;
  }



  public Integer indicatorVerify(AbstractList<Integer> sequence, Integer element) throws UnsupportedOperationException {
    // Example of using the Abstract class to generate an unsupported message.
    throw new UnsupportedOperationException(super.getUnsupportedMessage(this, "verifyElement <default>"));
  }
  public Integer indicatorVerify(AbstractList<Integer> sequence, Set<Integer> set, Integer element) throws UnsupportedOperationException {
    throw new UnsupportedOperationException(super.getUnsupportedMessage(this, "verifyElement <Set<Integer>>"));
  }
  public Integer indicatorVerify(AbstractList<Integer> sequence, Map<Integer, Integer> map, Integer element) throws UnsupportedOperationException {
    throw new UnsupportedOperationException(super.getUnsupportedMessage(this, "verifyElement <Map<Integer, Integer>>"));
  }
}
