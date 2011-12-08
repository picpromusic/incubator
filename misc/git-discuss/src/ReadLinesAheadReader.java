import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;


public class ReadLinesAheadReader implements ReadLinesAhead {

  private BufferedReader reader;
  private LinkedList<String> lineQueue;

  public ReadLinesAheadReader(Path path, Charset charset) throws IOException {
    this.reader = Files.newBufferedReader(path, charset);
    this.lineQueue = new LinkedList<>();
  }

  public String readLine() throws IOException {
    if (!lineQueue.isEmpty()) {
      return lineQueue.poll();
    } else {
      return reader.readLine();
    }
  }

  public String readAhead(int ahead) throws IOException {
    while (lineQueue.size() < ahead + 1) {
      String line = reader.readLine();
      if (line != null) {
        lineQueue.add(line);
      } else {
        break;
      }
    }
    if (lineQueue.size() > ahead) {
      return lineQueue.get(ahead);
    }
    return null;
  }

}
