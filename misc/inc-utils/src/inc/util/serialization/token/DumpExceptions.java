package inc.util.serialization.token;

import inc.util.HexDumpInputstream;
import inc.util.HexDumpOutputstream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import sun.misc.HexDumpEncoder;

public class DumpExceptions {

  private static final Class<?>[] DEFAULT = new Class[] {};
  private static final Class<?>[] MESSAGE_ONLY = new Class[] {String.class};
  private static final Class<?>[] CAUSE_ONLY = new Class[] {Throwable.class};
  private static final Class<?>[] CAUSE_EX_ONLY = new Class[] {Exception.class};
  private static final Class<?>[] CAUSE_IOEX_ONLY = new Class[] {IOException.class};
  private static final Class<?>[] BOTH = new Class[] {String.class,
      Throwable.class};
  private static LinkedList<Constructor<?>> notCalled;


  public static void main(String[] args) throws IOException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, ClassNotFoundException {
    String postfix = args[1];

    notCalled = new LinkedList<>();

    final List<String> classNames = new LinkedList<>();
    final Path start = Paths.get(args[2]);
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
          throws IOException {
        String name = file.getName(file.getNameCount() - 1).toString();
        if (name.endsWith("Exception.java") || name.endsWith("Error.java")) {
          Path relativize = start.relativize(file);
          StringBuilder sb = new StringBuilder();
          for (int i = 2; i < relativize.getNameCount(); i++) {
            sb.append('.');
            sb.append(relativize.getName(i));
          }
          CharSequence subSequence = sb.subSequence(1, sb.length() - 5);
          try {
            Class<?> forName = Class.forName(subSequence.toString());
            if (Throwable.class.isAssignableFrom(forName)) {
              classNames.add(subSequence.toString());
            }
          } catch (ClassNotFoundException e) {
          }
        }
        return super.visitFile(file, attrs);
      }

    });
    System.out.println(classNames.size());
    for (String name : classNames) {
      System.out.println(name);
      dumpOneClass(args, postfix, name);
    }

    System.out.println();
    for (Constructor<?> constructor : notCalled) {
      System.out.println(constructor);
    }

    System.out.println(notCalled.size());

  }

  private static void dumpOneClass(String[] args, String postfix,
      String className) throws ClassNotFoundException, IOException,
      InstantiationException, IllegalAccessException, InvocationTargetException {
    Class<?> forName = Class.forName(className);
    Class<? extends Throwable> eclass = (Class<? extends Throwable>) forName;

    File f = new File(args[0], eclass.getName().replace(".", "/"));
    f.mkdirs();

    Constructor<?>[] constructors = eclass.getConstructors();
    for (Constructor<?> constructor : constructors) {
      try {
        Throwable t = null;
        String name = "unknown";
        constructor.setAccessible(true);
        if (Arrays.equals(DEFAULT, constructor.getParameterTypes())) {
          t = (Throwable) constructor.newInstance();
          name = "default";
        } else if (Arrays.equals(MESSAGE_ONLY, constructor.getParameterTypes())) {
          t = (Throwable) constructor.newInstance("msg");
          name = "message";
        } else if (Arrays.equals(CAUSE_ONLY, constructor.getParameterTypes())) {
          t = (Throwable) constructor.newInstance(new RuntimeException("RTE"));
          name = "cause";
        } else if (Arrays.equals(CAUSE_EX_ONLY, constructor.getParameterTypes())) {
          t = (Throwable) constructor.newInstance(new RuntimeException("RTE"));
          name = "causeEx";
        } else if (Arrays.equals(CAUSE_IOEX_ONLY, constructor.getParameterTypes())) {
          t = (Throwable) constructor.newInstance(new IOException("IO"));
          name = "causeIOEx";
        } else if (Arrays.equals(BOTH, constructor.getParameterTypes())) {
          t =
              (Throwable) constructor.newInstance("msg", new RuntimeException(
                  "RTE"));
          name = "both";
        } else {
          notCalled.add(constructor);
        }
        if (t != null) {
          System.out.println("\t" + name);
          File fname = new File(f, name + ".dump." + postfix);
          ByteArrayOutputStream bout = new ByteArrayOutputStream();
//          HexDumpEncoder hde = new HexDumpEncoder();
          FileOutputStream fw = new FileOutputStream(fname);
          
//          byte[] byteArray = bout.toByteArray();
//          hde.encodeBuffer(byteArray, fw);
//          fw.close();
          
          HexDumpOutputstream hexOut = new HexDumpOutputstream(fw);
          ObjectOutputStream oo = new ObjectOutputStream(hexOut);
          oo.writeObject(t);
          oo.close();
          

//          try {
//            FileInputStream fin = new FileInputStream(fname);
//            HexDumpInputstream hexIn = new HexDumpInputstream(fin);
//            ObjectInputStream oi = new ObjectInputStream(hexIn);
//            Object readObject = oi.readObject();
//            System.out.println(readObject.getClass());
//          } catch (RuntimeException e) {
//            fname.delete();
//          }
        }
      } catch (Throwable t) {
        notCalled.add(constructor);
      }

    }
  }

}
