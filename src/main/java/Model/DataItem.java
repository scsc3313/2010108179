package model;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class DataItem<T> {
    private T data;
    private int sequence;

    public T getData(){
        return data;
    }
    public String getType(){
        return data.getClass().getName();
    }
    public int getSeq(){
        return sequence;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
