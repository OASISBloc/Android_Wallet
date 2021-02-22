package io.oasisbloc.wallet.ui.splash.viewmodel;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.model.DeviceModel;
import io.oasisbloc.wallet.ui.splash.SplashRoute;

public class SplashViewModel extends BaseViewModel {

    private static final long INTERVAL = App.isRelease() ? 2000L : 1000L;

    private MutableLiveData<SplashRoute> routeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    private Handler mHandler;
    private Runnable mCallback;

    private SplashViewModel() {
        mHandler = new Handler();
        mCallback = this::setRoute;
        messageLiveData.setValue(App.isProduction() ? "" : "Dev Server");
    }

    public static SplashViewModel get(FragmentActivity activity) {
        SplashViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SplashViewModel();
            }
        }).get(SplashViewModel.class);
        activity.getLifecycle().addObserver(vm);
        return vm;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.postDelayed(mCallback, INTERVAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mCallback);
    }

    public MutableLiveData<SplashRoute> getRouteLiveData() {
        return routeLiveData;
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    private void setRoute() {
        if (AccountModel.hasAccount()) {
            if (DeviceModel.isAppLockFirstLaunch()) {
                routeLiveData.setValue(SplashRoute.PIN_SETTING);
            } else {
                if (DeviceModel.isAppLockEnabled()) {
                    routeLiveData.setValue(SplashRoute.PIN_CHECKING);
                } else {
                    routeLiveData.setValue(SplashRoute.MAIN);
                }
            }
        } else {
            routeLiveData.setValue(SplashRoute.ACCOUNT);
        }
    }
}