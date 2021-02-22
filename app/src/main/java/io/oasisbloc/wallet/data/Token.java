package io.oasisbloc.wallet.data;

public class Token {

    private String symbol;
    private double balance;

    public Token(String symbol, double balance) {
        this.symbol = symbol;
        this.balance = balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getBalance() {
        return balance;
    }
}
