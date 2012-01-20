package inc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class HexDumpInputstream extends InputStream {

    private static final String BLANKS = "                                                                              ";
    private static boolean printValues = false;
    private static boolean printValuesVerbose = false;
    private BufferedReader ir;
    private byte[] buffer = new byte[16];
    int pos = 16;
    private String line;

    public HexDumpInputstream(InputStream os) {
	this.ir = new BufferedReader(new InputStreamReader(os));
    }

    @Override
    public int read() throws IOException {
	try {
	    if (pos < buffer.length) {
		if (printValuesVerbose) {
		    int startPos = 10 + 3 * (pos + 1) + ((pos + 1) > 8 ? 1 : 0);
		    int maxPos = line.length();
		    System.out.print(BLANKS.substring(0, startPos));

		    int p1 = Math.min(maxPos, 10 + 3 * (pos + 1)
			    + ((pos + 1) > 8 ? 1 : 0));
		    int p2 = Math.min(maxPos, 10 + 16 * 3 + 3);
		    System.out.print(line.substring(p1, p2));
		    System.out.print(BLANKS.substring(0, (pos + 1)));
		    p1 = Math.min(maxPos, 10 + 16 * 3 + 3 + (pos + 1));
		    p2 = Math.min(maxPos, 10 + 16 * 3 + 3 + 16 + 1);
		    System.out.println(line.substring(p1, p2));

		}
		return 0xff & buffer[pos++];
	    } else {
		line = ir.readLine();
		if (printValues) {
		    System.out.println(line);
		}
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

    public static void printValues(boolean verbose) {
	if (verbose) {
	    printValuesVerbose = true;
	}
	printValues = true;
    }

}
