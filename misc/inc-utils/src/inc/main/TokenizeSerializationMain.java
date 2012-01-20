package inc.main;

import inc.util.HexDumpInputstream;
import inc.util.serialization.token.Content;
import inc.util.serialization.token.ParsedToken;
import inc.util.serialization.token.SerializationTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TokenizeSerializationMain {

    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
        FileInputStream fin = new FileInputStream(args[0]);
        boolean hex = Boolean.parseBoolean(args[1]);
        InputStream in = fin;
    
        if (hex) {
            HexDumpInputstream.printValues(true);
            in = new HexDumpInputstream(fin);
        }
    
        // ObjectInputStream oin = new ObjectInputStream(in);
        // oin.readObject();
    
        SerializationTokenizer tokenizeSerialization = new SerializationTokenizer(
        	in);
        tokenizeSerialization.tokenize();
        List<Content> contents = tokenizeSerialization.getContents();
        for (Content content : contents) {
            content.printContent(System.out);
        }
        List<ParsedToken> tokens = tokenizeSerialization.getTokens();
        for (ParsedToken parsedToken : tokens) {
            System.out.println(parsedToken);
        }
    
    }

}
