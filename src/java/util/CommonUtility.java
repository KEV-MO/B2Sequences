package util;

import java.io.FileInputStream;
import java.util.ArrayList;

public class CommonUtility {
  /*
    Gets the initial sequence from a file if required.

    @param sequenceFile - Name of the sequence file.
    @param elementCount - Maximum number of elements to get.
  */
  public static ArrayList<Integer> getInitialSequence(String sequenceFile, int elementCount) {
    FileInputStream fis;
    ArrayList<Integer> initialSequence;

    if (sequenceFile != null) {
      fis = FileUtility.getStream(sequenceFile);
      if (elementCount > 0) {
        initialSequence = SequenceUtility.load(fis, elementCount);
      } else {
        initialSequence = SequenceUtility.load(fis);
      }
    } else {
      initialSequence = new ArrayList<>();
    }

    return initialSequence;
  }

  public static ArrayList<Integer> getSequence(String sequenceFile, int firstElement, int lastElement) {
    FileInputStream fis;
    ArrayList<Integer> initialSequence = null;

    if (sequenceFile != null) {
      fis = FileUtility.getStream(sequenceFile);
      if (lastElement - firstElement >= 0) {
        initialSequence = SequenceUtility.load(fis, lastElement - firstElement + 1, firstElement);
      }
    }

    if (initialSequence == null) {
      initialSequence = new ArrayList<>();
    }

    return initialSequence;
  }
}
