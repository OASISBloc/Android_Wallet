package io.oasisbloc.wallet.model.repository.remote;

import java.io.Serializable;

public class RemoteResult<T> implements Serializable {

    private boolean result;
    private String message;
    private T data;

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
