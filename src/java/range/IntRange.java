package range;

public class IntRange implements Range<Integer> {
  protected int lower;
  protected int upper;

  public IntRange(int lower, int upper) {
    this.lower = lower;
    this.upper = upper;
  }

  public Range<Integer> merge(Range<Integer> other) {
    Range<Integer> ret = null;
    if (canMerge(other)) {
      ret = new IntRange(Math.min(lower, other.getLower()), Math.max(upper, other.getUpper()));
    }
    return ret;
  }

  public boolean mergeIntoSelf(Range<Integer> other) {
    boolean ret = canMerge(other);
    if (ret) {
      this.lower = Math.min(lower, other.getLower());
      this.upper = Math.max(upper, other.getUpper());
    }
    return ret;
  }

  public Integer getLower() {
    return lower;
  }

  public Integer getUpper() {
    return upper;
  }

  public boolean canMerge(Range<Integer> other) {
    boolean ret = (other.getLower() <= this.upper + 1 || this.lower <= other.getUpper() + 1);
    // boolean ret = (other.compareTo(high + 1) == 0) || (other.compareTo(low - 1) == 0);
    return ret;
  }

  /*
  public int compareTo(Integer o) {
    return compareTo(o.intValue());
  }
  */

  public int compareTo(int i) {
    int ret = (i < lower ? -1 : (i <= upper ? 0 : 1));
    return ret;
  }

  public int compareTo(Range<Integer> o) {
    int ret = 0;
    if (o.getUpper() < lower) {
      ret = 1;
    } else if (upper < o.getLower()) {
      ret = -1;
    } else {
      ret = 0; // Only indicates that there is <some> overlap! This does not create a total ordering on all <code>IntRange</code>s
    }
    return ret;
  }

  public boolean contains(Range<Integer> other) {
    boolean ret = (compareTo(other.getLower()) == 0 && compareTo(other.getUpper()) == 0);
    return ret;
  }

  public Integer increment(Integer i) {
    return i + 1;
  }

  public boolean equals(Object obj) {
    boolean ret = false;
    if (obj instanceof IntRange) {
      ret = (compareTo((IntRange)obj) == 0);
    }
    return ret;
  }
}
