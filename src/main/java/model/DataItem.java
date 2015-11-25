package model;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class DataItem <T> {
    private final T data;
    private final int seqeunce;

    public DataItem(T data, int seqeunce) {
        this.data = data;
        this.seqeunce = seqeunce;
    }

    public Class<?> getType(){
        return data.getClass();
    }
    public T getData() {
        return data;
    }

    public int getSeqeunce() {
        return seqeunce;
    }
}
