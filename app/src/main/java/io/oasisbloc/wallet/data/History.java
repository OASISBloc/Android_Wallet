package io.oasisbloc.wallet.data;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.oasisbloc.wallet.base.DiffItemCallback;

public abstract class History implements Serializable, DiffItemCallback {

    protected static SimpleDateFormat TIMESTAMP_FORMAT_FROM
            = new SimpleDateFormat("MMM-dd-yyyy, hh:mm:ss a", Locale.ENGLISH);
    protected static SimpleDateFormat TIMESTAMP_FORMAT_TO
            = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);

    protected static final String TYPE_RECEIVE = "Received";
    protected static final String TYPE_SEND = "Sent";
    protected static final String TYPE_LOCKUP = "Transferlock";


    public abstract String getId();

    public abstract String getAccount();

    public abstract String getFromAccount();

    public abstract String getToAccount();

    public abstract String getDatetime();

    public abstract String getBeginDatetime();

    public abstract String getEndDatetime();

    public abstract String getBalance(String symbol);

    public abstract String getType();

    public abstract boolean isSend();

    public abstract boolean isReceive();

    public abstract boolean isLockup();

    public abstract String getMemo();

    public abstract String getSymbol();


    @Override
    public boolean areItemsTheSame(@NonNull Object object) {
        if (object instanceof History) {
            History item = (History) object;
            return getId().equalsIgnoreCase(item.getId());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Object object) {
        if (object instanceof History) {
            History item = (History) object;
            return getId().equalsIgnoreCase(item.getId());
        }
        return false;
    }
}
