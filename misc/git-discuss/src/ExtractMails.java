import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;


public class ExtractMails {
  public static void main(String[] args) throws IOException {
    int mails = 0;
    int messageIds = 0;
    Path path = Paths.get(args[0]);
    ReadLinesAheadReader rlar =
        new ReadLinesAheadReader(path, Charset.forName("US-ASCII"));
    SplitReader splitReader = new SplitReader(rlar, emailTestSpec());
    while (splitReader.switchToNextPart()) {
      int innerMessageIds = 0; 
      mails++;
      String line = splitReader.readLine();
      if (line != null) {
        System.out.println(line);
        if (line.startsWith("Message-ID: ")) {
          messageIds++;
          innerMessageIds++;
        }
      }
      while (line != null) {
        line = splitReader.readLine();
        if (line != null) {
          System.out.println(line);
          if (line.startsWith("Message-ID: ")) {
            messageIds++;
            innerMessageIds++;
          }
        }
      }
      if (innerMessageIds > 1) {
        System.out.println("STOP");
      }
      System.out.println("<<<<BREAK>>>>>>");
    }
    System.out.println(mails);
    System.out.println(messageIds);
  }

  private static SplitReader.SplitDetectorSpecifcation emailTestSpec() {
    return new SplitReader.SplitDetectorSpecifcation() {


      private Pattern[] pat;

      {
        pat = new Pattern[5];
        pat[0] = Pattern.compile("^$");
        pat[1] = Pattern.compile("^From.*$");
        pat[2] = Pattern.compile("^From\\:.*$");
        pat[3] = Pattern.compile("^Date\\:.*$");
        pat[4] = Pattern.compile("^Subject\\:.*$");
      }

      @Override
      public int getNumReadAhead() {
        return 5;
      }

      @Override
      public boolean check(int i, String line) {

        return pat[i].matcher(line).find();
      }
    };
  }
}
