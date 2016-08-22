package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileUtility {

  /*
    Creates a <code>FileInputStream</code> from a <code>File</code> created from from <code>name</code>.

    Returns <code>null</code> on failure.
  */
  public static FileInputStream getStream(String filename) {
    File file = new File(filename);
    FileInputStream stream = null;

    if (file.exists() && file.canRead()) {
      try {
        stream = new FileInputStream(file);
      } catch (FileNotFoundException fnfe) {
        stream = null;
      }
    }

    return stream;
  }
}
