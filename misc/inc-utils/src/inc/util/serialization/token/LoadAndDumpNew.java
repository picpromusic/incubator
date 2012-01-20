package inc.util.serialization.token;

import inc.util.HexDumpInputstream;
import inc.util.HexDumpOutputstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoadAndDumpNew {
  public static void main(String[] args) throws IOException,
      ClassNotFoundException {
    File dir = new File(args[0]);
    File[] listFiles = dir.listFiles();
    for (File file : listFiles) {
      System.out.println(file);
      try {
      FileInputStream fin = new FileInputStream(file);
      HexDumpInputstream hexIn = new HexDumpInputstream(fin);
      ObjectInputStream oin = new ObjectInputStream(hexIn);
      Object o = oin.readObject();
      oin.close();
      File dest =
          new File(file.getParentFile(), file.getName() + "." + args[1]);
      ObjectOutputStream oout =
          new ObjectOutputStream(new HexDumpOutputstream(new FileOutputStream(
              dest)));
      oout.writeObject(o);
      oout.close();
      } catch (Throwable th) {
        th.printStackTrace();
      }
    }
  }
}
