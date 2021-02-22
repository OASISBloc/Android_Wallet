package io.oasisbloc.wallet.model.repository.remote;

public class RemoteException extends Exception {

    private RemoteResult result;

    public RemoteException(RemoteResult result) {
        super(result.getMessage());
        this.result = result;
    }

    public RemoteResult getResult() {
        return result;
    }
}
