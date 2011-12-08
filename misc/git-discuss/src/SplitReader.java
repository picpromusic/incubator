import java.io.IOException;


public class SplitReader {

  public static interface SplitDetectorSpecifcation {

    int getNumReadAhead();

    boolean check(int i, String line);

  }

  private class SplitDetector {

    private SplitDetectorSpecifcation spec;
    private int lastReadAhead;

    public SplitDetector(SplitDetectorSpecifcation sdetSpec) {
      this.spec = sdetSpec;
    }

    boolean isSplit() throws IOException {
      int num = spec.getNumReadAhead();
      lastReadAhead = 0;
      boolean split = true;
      for (int i = 0; split && i < num; i++) {
        String line = rlar.readAhead(i);
        split &= line != null && spec.check(i, line);
      }

      if (!split) {
        for (int i = 1; i < num; i++) {
          String line = rlar.readAhead(i);
          this.lastReadAhead = num;
          if (line != null && spec.check(0, line)) {
            break;
          }
        }
      }
      return split;
    }

    int getLastReadAhead() {
      return lastReadAhead;
    }

  }

  private ReadLinesAheadReader rlar;
  private SplitDetector sdet;
  private boolean firstPart;

  public SplitReader(ReadLinesAheadReader rlar,
      SplitDetectorSpecifcation sdetSpec) {
    this.rlar = rlar;
    this.sdet = new SplitDetector(sdetSpec);
    this.firstPart = false;
  }

  public boolean switchToNextPart() throws IOException {
    if (!firstPart) {
      firstPart = true;
      return true;
    }
    while (!sdet.isSplit()) {
      String line = rlar.readLine();
      int readAhead = sdet.getLastReadAhead() - 1;
      while (line != null && readAhead > 0) {
        line = rlar.readLine();
        readAhead--;
      }
      return line != null;
    }
    rlar.readLine();
    return true;
  }

  public String readLine() throws IOException {
    if (!sdet.isSplit()) {
      return rlar.readLine();
    } else {
      return null;
    }
  }


}
