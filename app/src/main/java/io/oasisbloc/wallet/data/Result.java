package io.oasisbloc.wallet.data;

public class Result<T> {

    private T result;
    private Throwable error;

    public Result() {
        //noinspection unchecked
        this.result = (T) new Object();
    }

    public Result(T result) {
        this.result = result;
    }

    public Result(Throwable error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return result != null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public T getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

}
