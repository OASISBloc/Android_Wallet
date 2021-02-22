package io.oasisbloc.wallet.data;

import java.io.Serializable;

public class Account implements Serializable {

    private String account_name;

    private Limit cpu_limit;
    private Limit net_limit;
    private Limit ram_limit;

    private double ram_quota;
    private double ram_usage;
    private double ram_stake;

    private double net_weight;
    private double cpu_weight;

    public double getNetWeight() {
        return net_weight;
    }

    public double getCpuWeight() {
        return cpu_weight;
    }

    public double getRamStake() {
        return ram_stake;
    }

    public String getAccount() {
        return account_name;
    }

    public Limit getNetLimit() {
        return net_limit;
    }

    public Limit getCpuLimit() {
        return cpu_limit;
    }

    public Limit getRamLimit() {
        if (ram_limit == null) ram_limit = new Limit(ram_usage, ram_quota);
        return ram_limit;
    }
}
