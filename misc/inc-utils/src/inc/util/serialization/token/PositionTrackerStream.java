package inc.util.serialization.token;

import java.io.IOException;
import java.io.InputStream;

public class PositionTrackerStream extends InputStream{

  private InputStream in;
  private long pos;

  public PositionTrackerStream(InputStream in) {
    this.in = in;
    this.pos = 0;
  }

  @Override
  public synchronized int read() throws IOException {
    pos++;
    return in.read();
  }

  public long getPos() {
    return pos;
  }

}
