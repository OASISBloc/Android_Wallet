package io.oasisbloc.wallet.data;

import io.oasisbloc.wallet.base.DatetimeUtils;
import io.oasisbloc.wallet.base.DecimalUtils;

public class Lockup extends History {

    private static final String CLAIM_LOCKED = "0";
    private static final String CLAIM_RELEASED = "1";

    private String no;
    private String sender;
    private String receiver;
    private String token;
    private String memo;
    private String lock_begin;
    private String lock_end;
    private String claim;
    private String symbol;

    @Override
    public String getFromAccount() {
        return sender;
    }

    @Override
    public String getToAccount() {
        return receiver;
    }

    @Override
    public String getBeginDatetime() {
        long timestamp = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_begin, 0);
        return TIMESTAMP_FORMAT_TO.format(timestamp);
    }

    @Override
    public String getEndDatetime() {
        long timestamp = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_end, 0);
        return TIMESTAMP_FORMAT_TO.format(timestamp);
    }

    @Override
    public String getMemo() {
        return memo;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getId() {
        return no;
    }

    @Override
    public String getAccount() {
        return sender;
    }

    @Override
    public String getDatetime() {
        long timestamp = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_end, 0);
        return "End " + TIMESTAMP_FORMAT_TO.format(timestamp);
    }

    @Override
    public String getBalance(String symbol) {
        try {
            return DecimalUtils.formatWithSign(Double.parseDouble(token), symbol);
        } catch (Exception e) {
            return token;
        }
    }

    @Override
    public String getType() {
        return claim.equals(CLAIM_LOCKED) ? TYPE_LOCKUP : TYPE_RECEIVE;
    }

    @Override
    public boolean isSend() {
        return TYPE_SEND.equalsIgnoreCase(getType());
    }

    @Override
    public boolean isReceive() {
        return TYPE_RECEIVE.equalsIgnoreCase(getType());
    }

    @Override
    public boolean isLockup() {
        return TYPE_LOCKUP.equalsIgnoreCase(getType());
    }


    //
//    public String getNo() {
//        return no;
//    }
//
//    public String getSender() {
//        return sender;
//    }
//
//    public String getReceiver() {
//        return receiver;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public String getMemo() {
//        return memo;
//    }
//
//    public String getLockBegin() {
//        long timestamp = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_begin, 0);
//        return TIMESTAMP_FORMAT_TO.format(timestamp);
//    }
//
//    public String getLockEnd() {
//        long timestamp = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_end, 0);
//        return TIMESTAMP_FORMAT_TO.format(timestamp);
//    }
//
//    public boolean isClaimAble() {
//        long now = new Date().getTime();
//        long end = DatetimeUtils.timestamp(TIMESTAMP_FORMAT_FROM, lock_end, Long.MAX_VALUE);
//        boolean over = now > end;
//        return TYPE_LOCKED.equalsIgnoreCase(claim) && over;
//    }
//
//    public String getType() {
//        return claim.equals(TYPE_LOCKED) ? TYPE_LOCKUP : TYPE_RECEIVE;
//    }
//
//    public boolean isReceive() {
//        return TYPE_RECEIVE.equalsIgnoreCase(getType());
//    }
//
//    public boolean isSend() {
//        return TYPE_SEND.equalsIgnoreCase(getType());
//    }
//
//    public boolean isLock() {
//        return TYPE_LOCKUP.equalsIgnoreCase(getType());
//    }
//
//    @Override
//    public boolean areItemsTheSame(@NonNull Object object) {
//        if (object instanceof Lockup) {
//            Lockup item = (Lockup) object;
//            return no.equalsIgnoreCase(item.no);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean areContentsTheSame(@NonNull Object object) {
//        if (object instanceof Lockup) {
//            Lockup item = (Lockup) object;
//            return no.equalsIgnoreCase(item.no);
//        }
//        return false;
//    }
}
