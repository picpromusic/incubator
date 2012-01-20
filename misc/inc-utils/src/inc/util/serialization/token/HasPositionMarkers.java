package inc.util.serialization.token;

public interface HasPositionMarkers {

    public void setStartPos(long pos);

    public void setEndPos(long pos);

    public long getStartPos();

    public long getEndPos();

}
