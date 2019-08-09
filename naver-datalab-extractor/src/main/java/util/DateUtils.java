package util;

import exception.NaverSearchExtractorException;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    public String[] makeParameterArrayByYesterday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(yesterday());
        return date.split("/");
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public String getYesterdayString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(yesterday());
        return date;
    }

    public List<String[]> makeAllParameterArrayByStartDate(String startDate, String endDate) throws NaverSearchExtractorException, ParseException {

        List<String[]> parameterList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(endDate);

        DateTime startDateTime = new DateTime(start);
        DateTime endDateTime = new DateTime(end);

        long diff = (endDateTime.getMillis() - startDateTime.getMillis()) / 1000;
        long day = diff / (24 * 60 * 60);
        int loop = (int) (day / 7);
        for (int index = 0; index <= loop; index++) {
            parameterList.add(simpleDateFormat.format(startDateTime.plusDays(index * 7).toDate()).split("/"));
            parameterList.add(simpleDateFormat.format(startDateTime.plusDays((index * 7) + 6).toDate()).split("/"));
        }
        return parameterList;
    }
}
