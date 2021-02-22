package io.oasisbloc.wallet.data;

import java.util.List;

import io.oasisbloc.wallet.base.DatetimeUtils;
import io.oasisbloc.wallet.base.DecimalUtils;

public class Transaction extends History {

    private String id;
    private String type;
    private String blockNum;

    private double quantity;
    private String fromAccount;
    private String toAccount;

    private String memo;
    private String datetime;

    private String symbol;

    public static Transaction parse(List<String> source) {
        String[] raw = source.toArray(new String[0]);
        Transaction item = new Transaction();
        item.id = raw[0];
        item.blockNum = raw[1];
        item.datetime = DatetimeUtils.convert(TIMESTAMP_FORMAT_FROM, TIMESTAMP_FORMAT_TO, raw[2], 0);
        item.fromAccount = raw[3];
        item.toAccount = raw[4];
        item.memo = raw[5];
        item.quantity = Double.parseDouble(raw[6]);
        item.type = raw[7];
        if (item.isSend()) item.quantity = item.quantity * -1;
        item.symbol = raw[8];
        return item;
    }

    @Override
    public String getFromAccount() {
        return fromAccount;
    }

    @Override
    public String getToAccount() {
        return toAccount;
    }

    @Override
    public String getBeginDatetime() {
        return getDatetime();
    }

    @Override
    public String getEndDatetime() {
        return getDatetime();
    }

    @Override
    public String getMemo() {
        return memo;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSymbol(){
        return symbol;
    }

    @Override
    public String getAccount() {
        if (isSend()) return toAccount;
        if (isReceive()) return fromAccount;
        if (isLockup()) return fromAccount;
        return toAccount;
    }

    @Override
    public String getDatetime() {
        return datetime;
    }

    @Override
    public String getBalance(String symbol) {
        return DecimalUtils.formatWithSign(quantity, symbol);
    }

    @Override
    public String getType() {
        if (type == null || type.length() == 1) {
            return "";
        }
        return type.substring(0, 1).toUpperCase() + type.substring(1);
    }

    @Override
    public boolean isSend() {
        return TYPE_SEND.equalsIgnoreCase(type);
    }

    @Override
    public boolean isReceive() {
        return TYPE_RECEIVE.equalsIgnoreCase(type);
    }

    @Override
    public boolean isLockup() {
        return TYPE_LOCKUP.equalsIgnoreCase(type);
    }

    //
//    public String getId() {
//        return id;
//    }
//
//    public boolean isReceive() {
//        return TYPE_RECEIVE.equalsIgnoreCase(type);
//    }
//
//    public boolean isSend() {
//        return TYPE_SEND.equalsIgnoreCase(type);
//    }
//
//    public boolean isLockup() {
//        return TYPE_LOCKUP.equalsIgnoreCase(type);
//    }
//
//    public String getType() {
//        if (type == null || type.length() == 1) {
//            return "";
//        }
//        return type.substring(0, 1).toUpperCase() + type.substring(1);
//    }
//
//    public String getBlockNum() {
//        return blockNum;
//    }
//
//    public double getQuantity() {
//        return quantity;
//    }
//
//    public String getFromAccount() {
//        return fromAccount;
//    }
//
//    public String getToAccount() {
//        return toAccount;
//    }
//
//    public String getTargetAccount() {
//        if (isSend()) return toAccount;
//        if (isReceive()) return fromAccount;
//        return toAccount;
//    }
//
//    public String getMemo() {
//        return memo;
//    }
//
//    public String getDatetime() {
//        return datetime;
//    }
//
//    public String getSymbol() {
//        return "OSB";
//    }
//
//    @Override
//    public boolean areItemsTheSame(@NonNull Object object) {
//        if (object instanceof Transaction) {
//            Transaction item = (Transaction) object;
//            return id.equalsIgnoreCase(item.id);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean areContentsTheSame(@NonNull Object object) {
//        if (object instanceof Transaction) {
//            Transaction item = (Transaction) object;
//            return id.equalsIgnoreCase(item.id);
//        }
//        return false;
//    }
}
