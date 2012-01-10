import java.io.File;

import org.eclipse.jgit.api.InitCommand;


public class GitTests {
public static void main(String[] args) {
    InitCommand initc = new InitCommand();
    initc.setBare(true);
    initc.setDirectory(new File("./git-test"));
    initc.call();
}
}
