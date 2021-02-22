package io.oasisbloc.wallet.data;

import java.io.Serializable;

public class Limit implements Serializable {

    private double used;
    private double available;
    private double max;

    public Limit(double used, double max) {
        this.used = used;
        this.available = max - used;
        this.max = max;
    }

    public double getUsed() {
        return used;
    }

    public double getAvailable() {
        return available;
    }

    public double getMax() {
        return max;
    }

    public double getPercent() {
        return available / max;
    }
}
