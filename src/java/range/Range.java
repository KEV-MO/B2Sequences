package range;

public interface Range<T> extends Comparable<Range<T>> {

// public boolean canMerge(Range<T> other);
  public T getLower();
  public T getUpper();

  public boolean canMerge(Range<T> other);
  public Range<T> merge(Range<T> other);
  public boolean mergeIntoSelf(Range<T> other);

  // public int compareTo(T o);
  public int compareTo(Range<T> o);

  public boolean contains(Range<Integer> other);

  public T increment(T t);
}
