package verifier;

import java.util.AbstractList;
import java.util.Map;
import java.util.Set;

/*
  Interface for an object that can verify if an element can be added to a
    sequence.
*/
public interface SequenceVerifier<T> {
  public T upperBound(int index);
  public T lowerBound(int index);

  // Default verification
  public boolean verifyElement(AbstractList<T> sequence, T element);
  // Verification with an extra <code>Set</code> of data. May not be necessary but can be used to achieve a speed-up in some cases.
  public boolean verifyElement(AbstractList<T> sequence, Set<T> set, T elements);
  // Verification with an extra <code>Map</code> of data. May not be necessary but can be used to achieve a speed-up in some cases.
  public boolean verifyElement(AbstractList<T> sequence, Map<T, T> map, T element);

  /*
    Default verification with an indicator.

    The indicator should be used to pass information about the next term in the
      sequence to the caller.
  */
  public T indicatorVerify(AbstractList<T> sequence, T element);
  // IndicatorVerification with an extra <code>Set</code> of data.
  public T indicatorVerify(AbstractList<T> sequence, Set<T> set, T element);
  // Verification with an extra <code>Map</code> of data.
  public T indicatorVerify(AbstractList<T> sequence, Map<T, T> map, T element);
}
