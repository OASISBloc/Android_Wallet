package io.oasisbloc.wallet.model.repository.remote;

import java.io.Serializable;
import java.util.List;

import io.oasisbloc.wallet.data.Lockup;

public class LockupHistoryRemoteResult extends RemoteResult<List<Lockup>> implements Serializable {

    private double totalToken;

    public double getTotalBalance() {
        return totalToken;
    }
}
