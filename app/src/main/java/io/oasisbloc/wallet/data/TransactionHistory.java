package io.oasisbloc.wallet.data;

import java.util.ArrayList;

public class TransactionHistory extends ArrayList<Transaction> {

    private double total;

    public void setTotalBalance(double total) {
        this.total = total;
    }

    public double getTotalBalance() {
        return total;
    }
}
