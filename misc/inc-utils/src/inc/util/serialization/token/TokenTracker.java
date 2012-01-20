package inc.util.serialization.token;

import java.util.ArrayList;
import java.util.List;

public class TokenTracker {

    private ArrayList<ParsedToken> tokens;

    public TokenTracker(PositionTrackerStream position) {
	this.tokens = new ArrayList<>();
    }

    public List<ParsedToken> getTokens() {
	return this.tokens;
    }

    void addTypeToken(byte type) {
	tokens.add(new TypeToken(type));
    }

    void addNewHandleToken(int i) {
	tokens.add(new NewHandleToken(i));
    }
    
    void addReferenceToken(int handle) {
	tokens.add(new ReferenceToken(handle));
    }

    void addMagicToken(ParsedWrapper<Short> magic) {
	tokens.add(new MagicToken(magic));
    }

    public void addVersionToken(ParsedWrapper<Short> version) {
	tokens.add(new VersionToken(version));
    }

}
