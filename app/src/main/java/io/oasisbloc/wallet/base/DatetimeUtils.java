package io.oasisbloc.wallet.base;

import java.text.DateFormat;
import java.util.Date;

public class DatetimeUtils {

    public static long timestamp(DateFormat format, String source, long defaultValue) {
        try {
            return format.parse(source).getTime();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static String convert(DateFormat from, DateFormat to, String source, long defaultValue) {
        try {
            Date temp = from.parse(source);
            return to.format(temp);
        } catch (Exception e) {
            return to.format(new Date(defaultValue));
        }
    }
}
