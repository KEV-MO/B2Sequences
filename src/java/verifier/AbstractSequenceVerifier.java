package verifier;

import java.util.AbstractList;
import java.util.Map;
import java.util.Set;


/*
  Abstract class to assist in building messages for unsupported function
    exceptions.
*/
public abstract class AbstractSequenceVerifier<T> implements SequenceVerifier<T> {
  public abstract T upperBound(int index);
  public abstract T lowerBound(int index);

  public abstract boolean verifyElement(AbstractList<T> sequence, T element);
  public abstract boolean verifyElement(AbstractList<T> sequence, Set<T> set, T elements);
  public abstract boolean verifyElement(AbstractList<T> sequence, Map<T, T> map, T element);

  /*
    Builds a default string for unsupprted functions exceptions for use by
      <code>SequenceVerifier</code> implementations.

    @param object - Verifier that does not implement a method.
    @param methodName - String name of the function/method that is not
      implemented.
  */
  public String getUnsupportedMessage(Object object, String methodName) {
    StringBuilder sb = new StringBuilder();
    Class objectClass = object.getClass();
    sb.append(objectClass.getPackage());
    sb.append('.');
    sb.append(objectClass.getName());
    sb.append('@');
    sb.append(methodName);
    sb.append(" is not implemented for this verifier.");
    return sb.toString();
  }

  public abstract T indicatorVerify(AbstractList<T> sequence, T element);
  public abstract T indicatorVerify(AbstractList<T> sequence, Set<T> set, T element);
  public abstract T indicatorVerify(AbstractList<T> sequence, Map<T, T> map, T element);
}
