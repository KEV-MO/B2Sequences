package range;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class RangeSet<T extends Comparable> implements Set<T> {
  protected TreeMap<Range<T>, Range<T>> map;

  protected void initMap() {
    map = new TreeMap<Range<T>, Range<T>>();
  }

  protected void initMapRange(Collection<? extends Range<T>> c) {
    initMap();
    for (Range<T> r : c) {
      map.put(r, r);
    }
  }

  protected void initMap(Collection<? extends T> c) {
    initMap();
    addAll(c);
  }

  public abstract boolean add(T t);

  public boolean addAll(Collection<? extends T> c) {
    boolean ret = false;
    for (T r : c) {
      ret = add(r) || ret;
    }
    return ret;
  }

  public void clear() {
    map.clear();
  }

  public abstract boolean contains(Object o);

  public boolean containsAll(Collection<?> o) {
    boolean ret = true;
    Iterator iter = o.iterator();
    Object obj;
    while (iter.hasNext() && ret) {
      obj = iter.next();
      ret = contains(obj);
    }
    return ret;
  }

  public boolean equals(Object o) {
    return false;
  }

  public int hashCode() {
    return map.hashCode();
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public Iterator<T> iterator() {
    // return new RangeIterator<T>(map.keySet().iterator())
    return null;
  }

  /*
  public static class RangeIterator<E> implements Iterator<E> {
    public RangeIterator(Iterator<Range<E>> iter) {

    }
  }
  */

  public abstract boolean remove(Object o);

  public boolean removeAll(Collection<?> c) {
    return false;
  }

  public boolean retainAll(Collection<?> c) {
    return false;
  }

  public int size() {
    return map.size();
  }

  public Object[] toArray() {
    return null;
  }

  public <T> T[] toArray(T[] a) {
    return null;
  }

  public abstract Range<Integer> minimumRange();

  public abstract Range<Integer> getRange(Integer t);
}
