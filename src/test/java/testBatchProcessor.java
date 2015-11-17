import org.junit.Test;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class testBatchProcessor {
    @Test
    public void 배치를_실행한다(){
        DataReader dataReader = null;
        DataProcessor dataProcessor = null;
        BatchProcessor sut = new BatchProcessor(dataReader, dataProcessor);

        sut.doBatch();
        ProcessResult result = sut.getResult();

        assertThat(result.getErrorCode(), is(0));
    }
}
