package range;

import java.util.Set;

public class RangeUtility {

  public static int getMinimumNotIn(Set<Integer> set, int i) {
    int ret = Integer.MAX_VALUE;
    if (set instanceof IntRangeSet) {
      Range<Integer> range = ((IntRangeSet)set).getRange(i);
      if (range == null) {
        ret = i;
      } else {
        ret = range.getUpper() + 1;
      }
    } else {
      int s = i;
      while (set.contains(s)) {
        s++;
      }
      ret = s;
    }
    return ret;
  }

  public static int getMinimumNotIn(Set<Integer> set) {
    return getMinimumNotIn(set, 1);
  }
}
