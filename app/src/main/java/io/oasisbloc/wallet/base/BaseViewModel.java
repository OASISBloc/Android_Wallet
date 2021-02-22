package io.oasisbloc.wallet.base;

import android.content.Context;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel implements LifecycleObserver {

    protected Disposable disposable;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }


    public int getInteger(Context context, @IntegerRes int id) {
        return context.getResources().getInteger(id);
    }

    public String getString(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }
}
