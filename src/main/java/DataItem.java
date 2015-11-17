/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public interface DataItem<T> {
    String getType();
    int getSeq();
    T getData();
}
