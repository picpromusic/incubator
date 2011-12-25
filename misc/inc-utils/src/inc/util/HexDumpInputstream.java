package inc.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class HexDumpInputstream extends InputStream {

  private BufferedReader ir;
  private byte[] buffer = new byte[16];
  int pos = 16;


  public HexDumpInputstream(InputStream os) {
    this.ir = new BufferedReader(new InputStreamReader(os));
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
          StringBuilder sb = new StringBuilder(line.substring(10, 33));
          sb.append(line.substring(34, 58));
          String[] split = sb.toString().trim().split(" ");
          pos = buffer.length - split.length;
          for (int i = split.length - 1; i >= 0; i--) {
            buffer[pos + i] = (byte) Integer.parseInt(split[i], 16);
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
