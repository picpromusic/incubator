package inc.myFirstActor;

import java.io.IOException;
import java.util.concurrent.Future;

@FunctionalInterface
public interface ThrowsException {
	Future<Void> throwsException(boolean twice) throws IOException;
}
