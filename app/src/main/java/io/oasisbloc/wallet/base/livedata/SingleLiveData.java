package io.oasisbloc.wallet.base.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import io.oasisbloc.wallet.base.action.Action0;
import io.oasisbloc.wallet.base.action.Action1;

public class SingleLiveData<T> {

    private final MutableLiveData<T> mSuccess = new MutableLiveData<>();
    private final MutableLiveData<Throwable> mError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>();


    public SingleLiveData() {
    }


    /*
        observe
     */

    public void observe(@NonNull LifecycleOwner owner,
                        Action1<T> success,
                        Action1<Throwable> error,
                        Action1<Boolean> loading) {
        if (success != null) mSuccess.observe(owner, t -> {
            if (t != null) success.onAction(t);
        });
        if (error != null) mError.observe(owner, e -> {
            if (e != null) error.onAction(e);
        });
        if (loading != null) this.mLoading.observe(owner, l -> {
            if (l != null) loading.onAction(l);
        });
    }

    public void observe(@NonNull LifecycleOwner owner,
                        Action0 success,
                        Action1<Throwable> error,
                        Action1<Boolean> loading) {
        if (success != null) mSuccess.observe(owner, t -> {
            if (t != null) success.onAction();
        });
        if (error != null) mError.observe(owner, e -> {
            if (e != null) error.onAction(e);
        });
        if (loading != null) this.mLoading.observe(owner, l -> {
            if (l != null) loading.onAction(l);
        });
    }

    /*
        result
     */

    public void setResult(T value, Throwable error) {
        mSuccess.postValue(value);
        mError.postValue(error);
    }

    /*
        loading
     */

    public void setLoading(boolean value) {
        mLoading.postValue(value);
    }
}
