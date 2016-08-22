package range;

import java.util.Collection;

public class IntRangeSet extends RangeSet<Integer> {
  public IntRangeSet() {
    super.initMap();
  }

  public IntRangeSet(Collection<? extends Integer> c) {
    super.initMap(c);
  }

  public IntRangeSet(Collection<? extends Range<Integer>> c, Range<Integer> r) {
    super.initMapRange(c);
  }

  public boolean contains(Object o) {
    boolean ret = false;
    if (o instanceof Integer) {
      Range<Integer> range = new IntRange((Integer)o, (Integer)o);
      ret = map.containsKey(range);
    }
    return ret;
  }

  public boolean add(Integer t) {
    Range<Integer> range = new IntRange(t, t);
    Range<Integer> tRange;
    Range<Integer> upper = null;
    Range<Integer> lower = null;
    boolean ret = false;
    if (!map.containsKey(range)) {
      ret = true;
      tRange = new IntRange(t + 1, t + 1);
      upper = map.remove(tRange);
      tRange = new IntRange(t - 1, t - 1);
      lower = map.remove(tRange);
      if (upper != null) {
        upper.mergeIntoSelf(range);
        if (lower == null) {
          map.put(upper, upper);
        }
      }
      if (lower != null) {
        lower.mergeIntoSelf(range);
        if (upper == null) {
          map.put(lower, lower);
        }
      }
      if (lower != null && upper != null) {
        upper.mergeIntoSelf(lower);
        map.put(upper, upper);
      }
      if (upper == null && lower == null) {
        map.put(range, range);
      }
    }
    return ret;
  }

  public boolean remove(Object o) {
    return map.remove(o) != null;
  }

  public Range<Integer> minimumRange() {
    return map.firstKey();
  }

  public Range<Integer> getRange(Integer t) {
    Range<Integer> range = new IntRange(t, t);
    Range<Integer> ret;
    ret = map.get(range);
    return ret;
  }

  public static void main(String [] args) {
    IntRangeSet rangeSet = new IntRangeSet();
    rangeSet.add(1);

    System.out.println("contains 1 : " + rangeSet.contains(1));

    rangeSet.add(3);
    rangeSet.add(5);
    rangeSet.add(7);
    rangeSet.add(10);

    System.out.println("contains 10 : " + rangeSet.contains(10));
    System.out.println("not contains 6 : " + rangeSet.contains(6));

    rangeSet.add(2);
    rangeSet.add(4);
    rangeSet.add(6);

    System.out.println("contains 3 : " + rangeSet.contains(3));

    Range<Integer> range = rangeSet.minimumRange();
    System.out.println("greatest not in set : " + (range.getUpper() + 1));
  }
}
