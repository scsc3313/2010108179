package term.project;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import model.DataItem;
import model.ProcessResult;
import processor.DataProcessor;
import processor.ProcessFailException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class StringLineSortProcessor implements DataProcessor<String> {

    public ProcessResult<String> processItem(DataItem<String> item) throws ProcessFailException {
        String data = item.getData();
        Date startTime = new Date();
        String sortData;
        boolean isSuccess = false;
        try{
            sortData = sort(data);
            isSuccess = true;
        }
        catch (NumberFormatException e){
            sortData = item.getData();
            isSuccess = false;
        }

        DataItem<String> processedItem = new DataItem<>(
                sortData, item.getSeqeunce()
        );

        Date endTime = new Date();

        ProcessResult<String> result = new ProcessResult<>(
                processedItem,
                startTime,
                endTime,
                isSuccess,
                getClass().getName()
        );

        return result;
    }

    private String sort(String targetString){
        String[] items = targetString.split(" ");
        List<Integer> numberList = new ArrayList<Integer>();

        for (String item : items){
            numberList.add(Integer.parseInt(item));
        }

        numberList.sort(Comparator.<Integer>naturalOrder());

        StringBuilder builder = new StringBuilder();
        for (Integer number : numberList){
            builder.append(number)
                    .append(' ');
        }

        return builder.toString();
    }
}
