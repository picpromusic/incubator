package inc.util;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class HexDumpOutputstream extends OutputStream {

  private BufferedWriter ow;
  private byte[] buffer = new byte[16];
  private int pos = 0;
  private int adress = 0;
  private HexDumpPattern.FieldDescription[] fieldDescriptions;

  public HexDumpOutputstream(OutputStream os,HexDumpPattern hdp) {
    this.fieldDescriptions = hdp.getFields();
    this.ow = new BufferedWriter(new OutputStreamWriter(os));
  }


  public HexDumpOutputstream(OutputStream os) {
    this(os, HexDumpPattern.DEFAULT);
  }

  @Override
  public void write(int b) throws IOException {
    buffer[pos++] = (byte) b;
    if (pos == 16) {
      writeArray(buffer);
      pos = 0;
    }
  }

  private void writeArray(byte[] array) throws IOException {
    StringBuilder line = new StringBuilder(120);
    String adressString = zeroAdressPattern + Integer.toHexString(adress);
    adressString = adressString.substring(adressString.length() - 8);
    line.append(adressString);
    line.append("  ");
    appendContent(array, 0, 0, 8, line);
    appendContent(array, 0, 8, 8, line);
    line.append("                                                            ");
    line.setLength(60);
    appendChars(array, 0, 0, 16, line);
    adress+=16;

    ow.append(line.toString());
    ow.append("\r\n");
  }

  @Override
  public void close() throws IOException {
    byte[] sbuf = new byte[pos];
    System.arraycopy(buffer, 0, sbuf, 0, pos);
    writeArray(sbuf);
    ow.close();
  }

  private static void appendChars(byte[] byteArray, int i, int base, int size,
      StringBuilder line) {
    line.append("|");
    for (int j = base; j < base + size && j + i < byteArray.length; j++) {
      byteArray[j + i] = byteArray[j + i] < 32 //
          ? 46 // 46 --> '.'
          : (byteArray[j + i] > 126//
              ? 46 // 46 --> '.'
              : byteArray[j + i]);
      line.append((char) byteArray[j + i]);
    }
    line.append("|");
  }

  private static void appendContent(byte[] byteArray, int i, int base,
      int size, StringBuilder line) {
    for (int j = base; j < base + size && j + i < byteArray.length; j++) {
      String value =
          zeroAdressPattern + Integer.toHexString(0xff & byteArray[i + j]);
      value = value.substring(value.length() - 2);
      line.append(value);
      line.append(' ');
    }
    line.append(' ');
  }

}
