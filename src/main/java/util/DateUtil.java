package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class DateUtil {
    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

        return formatter.format(date);
    }
    public static long elapsedTime(Date before, Date after){
        return after.getTime()-before.getTime();
    }

}
