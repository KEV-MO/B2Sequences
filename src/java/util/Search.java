package util;

import java.util.AbstractList;

public class Search {

  /*
    Binary search returning the index of the query or the index of the largest
      value less than <code>query</code> in <code>sortedList</code>.
    An exception to this is when <code>query</code> is below the range
      <code>[sortedList.get(lower), sortedList.get(upper)]</code>. In this case
      <code>lower</code> is returned.

    Behavior undefined if <code>sortedList</code> is not sorted.

    @param sortedList - Sorted list of integers. Behavior undefined if <code>sortedList</code>
      is not sorted.
    @param query - Value to search for in <code>sortedList</code>.
    @param lower - Lowest index to search.
    @param upper - Highest index to search.
  */
  public static final int binarySearchIndex(AbstractList<Integer> sortedList, int query, int lower, int upper) {
    int mid;
    int ret = Integer.MIN_VALUE;
    while (lower <= upper) {
      mid = (lower + upper) >> 1; // Two's complement bit shift is equivalent to dividing by 2 but is much faster (~37 times).
      // Additionally, lower + (upper - lower) / 2 = upper / 2 - lower / 2 + lower = upper / 2 + lower / 2 = (upper + lower) / 2
      if (query == sortedList.get(mid)) {
        ret = mid;
        break;
      } else if (query < sortedList.get(mid)) {
        upper = mid - 1;
      } else {
        lower = mid + 1;
      }
    }
    if (ret == Integer.MIN_VALUE) {
      ret = lower;
    }
    return ret;
  }

  /*
    Binary search returning the index of the <code>query</code> or <code>Integer.MIN_VALUE</code> if the
      <code>query</code> is not found.

    Behavior undefined if <code>sortedList</code> is not sorted.

    @param sortedList - Sorted list of integers. Behavior undefined if <code>sortedList</code>
      is not sorted.
    @param query - Value to search for in <code>sortedList</code>.
    @param lower - Lowest index to search.
    @param upper - Highest index to search.
  */
  public static final int binarySearchFind(AbstractList<Integer> sortedList, int query, int lower, int upper) {
    int ret = binarySearchIndex(sortedList, query, lower, upper);
    if (ret < 0 || ret >= sortedList.size() || sortedList.get(ret) != query) {
      ret = Integer.MIN_VALUE;
    }
    return ret;
  }

  /*
    Binary search returning a boolean indicating if a value is found in <code>sortedList</code>.

    Behavior undefined if <code>sortedList</code> is not sorted.

    @param sortedList - Sorted list of integers. Behavior undefined if <code>sortedList</code>
      is not sorted.
    @param query - Value to search for in <code>sortedList</code>.
    @param lower - Lowest index to search.
    @param upper - Highest index to search.
  */
  public static final boolean binarySearch(AbstractList<Integer> sortedList, int query, int lower, int upper) {
    return Integer.MIN_VALUE != Search.binarySearchFind(sortedList, query, lower, upper);
  }
}
