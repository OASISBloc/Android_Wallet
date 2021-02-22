package io.oasisbloc.wallet.base;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class DecimalUtils {

    private static final DecimalFormat DECIMAL_FORMAT_2;
    private static final DecimalFormat DECIMAL_FORMAT_8;
    private static final DecimalFormat PERCENT_FORMAT;

    static {
        DECIMAL_FORMAT_2 = new DecimalFormat("###,###,###,###,###,###,###.##");
        DECIMAL_FORMAT_8 = new DecimalFormat("###,###,###,###,###,###,###.########");
        PERCENT_FORMAT = new DecimalFormat("###,###,###,###,###,###,###.##%");
        PERCENT_FORMAT.setRoundingMode(RoundingMode.FLOOR);
    }

    public static String formatWithSign(double value, String symbol) {
        return (value > 0 ? "+" : "")
                + DECIMAL_FORMAT_8.format(value)
                + " "
                + symbol.toUpperCase();
    }

    public static String to(double value) {
        return DECIMAL_FORMAT_8.format(value);
    }

    public static String to(double value, String suffix) {
        return DECIMAL_FORMAT_8.format(value) + " " + suffix;
    }

    public static String toPercent(double value) {
        return PERCENT_FORMAT.format(value);
    }

    public static String toVolume(double value) {
        if (value > 1000) {
            return DECIMAL_FORMAT_2.format(value / 1024) + "KB";
        }
        return DECIMAL_FORMAT_8.format(value);
    }

    public static String toSpeed(double value) {
        if (value > 1000) {
            return DECIMAL_FORMAT_2.format(value / 1000) + "ms";
        }
        return DECIMAL_FORMAT_8.format(value);
    }
}
