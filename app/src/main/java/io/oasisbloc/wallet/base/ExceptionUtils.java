package io.oasisbloc.wallet.base;

public class ExceptionUtils {

    public static String getMessage(Throwable throwable) {
        if (throwable instanceof com.google.gson.JsonSyntaxException) {
            return "Json Syntax Exception";
        } else if (throwable instanceof java.net.UnknownHostException) {
            return "Unknown Host Exception";
        } else if (throwable instanceof java.net.ConnectException) {
            return "Connect Exception";
        }

        return throwable.getMessage();
    }
}
