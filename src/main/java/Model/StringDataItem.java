package Model;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class StringDataItem implements DataItem<String>{
    private String data;
    private int seqNo;

    public String getType() {
        return getClass().getCanonicalName();
    }

    public int getSeq() {
        return seqNo;
    }

    public String getData() {
        return data;
    }
}
