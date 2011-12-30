package inc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class HexDumpInputstream extends InputStream {

  private final BufferedReader ir;
  private final byte[] buffer = new byte[16];
  private final int startPos;
  private final int endPos;
  private final int numberOfBytesPerLine;
  int pos = 16;

  public HexDumpInputstream(InputStream is, int startPos, int endPos,
      int numberOfBytesPerLine) {
    this.startPos = startPos;
    this.endPos = endPos;
    this.numberOfBytesPerLine = numberOfBytesPerLine;
    this.pos = numberOfBytesPerLine;
    this.ir = new BufferedReader(new InputStreamReader(is));
  }

  public HexDumpInputstream(InputStream is, int startPos, int endPos) {
    this(is, startPos, endPos, 16);
  }

  public HexDumpInputstream(InputStream is) {
    this(is, 10, 58);
  }

  @Override
  public int read() throws IOException {
    try {
      if (pos < buffer.length) {
        return 0xff & buffer[pos++];
      } else {
        String line = ir.readLine();
        if (line == null) {
          return -1;
        } else {
          String[] split = line//
              .substring(startPos, endPos)//
              .replace("  ", " ")//
              .trim()//
              .split(" ");
          pos = buffer.length - split.length;
          for (int i = split.length - 1; i >= 0; i--) {
            buffer[pos + i] = (byte) Integer.parseInt(split[i]);
          }
          return read();
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new RuntimeException(t);
    }
  }

  @Override
  public void close() throws IOException {
    this.ir.close();
    super.close();
  }
}
