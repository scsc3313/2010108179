package processor;

import model.DataItem;
import model.ProcessResult;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class TestStringLineSortProcessor {
    private StringLineSortProcessor sut;

    @Before
    public void setUp() throws Exception {
        sut = new StringLineSortProcessor();
    }

    @Test
    public void 테스트_결과의_항목들이_정상적인지_검사() throws Exception {
        DataItem<String> mockItem = getMockItem(0);

        ProcessResult<String> result = sut.processItem(mockItem);

        assertThat(result.getData(), notNullValue());
        assertThat(result.getStartTime().getTime(), greaterThanOrEqualTo(result.getEndTime().getTime()));
        assertThat(result.getProcessorName(), is(sut.getClass().getName()));
    }

    @Test
    public void 실제로_문자열이_정렬되는지_검사() throws Exception {
        DataItem<String> mockItem = getMockItem(0);

        ProcessResult<String> result = sut.processItem(mockItem);
        DataItem<String> processedData = result.getData();
        String data = processedData.getData();

        assertThat(result, notNullValue());
        assertThat("sorted string is"+result.getData().getData(), isElementSorted(data), is(true));
    }

    @Test
    public void 잘못된_문자열이_입력되었을때_실패하는지_검사() throws Exception {
        DataItem<String> mockItem = new DataItem<>("이것은 잘못된 문자열 입니다", 0);

        ProcessResult<String> result = sut.processItem(mockItem);

        assertThat(result.isSuccess(), is(false));

    }

    private boolean isElementSorted(String targetString){
        String[] split = targetString.split(" ");

        if (split.length == 0)
            return true;

        int maxNumber = Integer.MIN_VALUE;
        for (String string : split){
            int numberFromString = Integer.parseInt(string);
            if (numberFromString >= maxNumber)
                maxNumber = numberFromString;
            else
                return false;
        }
        return true;
    }
    private DataItem<String> getMockItem(int sequence){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++){
            int number = (int) (Math.random() * 100);

            builder.append(number)
                    .append(' ');
        }

        String randomNumberString = builder.toString();

        DataItem<String> mockItem = new DataItem<String>(
                randomNumberString,
                sequence
        );

        return mockItem;
    }


}
