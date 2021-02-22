package io.oasisbloc.wallet.viewmodel;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.model.DeviceModel;

public class SettingsViewModel extends BaseViewModel {

    private Context mContext;

    private MutableLiveData<String> account = new MutableLiveData<>();
    private MutableLiveData<Boolean> appLockEnabled = new MutableLiveData<>();
    private MutableLiveData<String> version = new MutableLiveData<>();
    private MutableLiveData<Result> logoutResult = new MutableLiveData<>();

    public static SettingsViewModel get(FragmentActivity activity, LifecycleOwner lifecycleOwner) {
        SettingsViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SettingsViewModel(activity);
            }
        }).get(SettingsViewModel.class);
        lifecycleOwner.getLifecycle().addObserver(vm);

        return vm;
    }

    private SettingsViewModel(Context context) {
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        account.setValue(AccountModel.getAccount());
        appLockEnabled.setValue(DeviceModel.isAppLockEnabled());
        version.setValue(App.getVersion());
    }

    /*
        getter
     */

    public MutableLiveData<String> getAccount() {
        return account;
    }

    public MutableLiveData<String> getVersion() {
        return version;
    }

    public MutableLiveData<Boolean> getAppLockEnabled() {
        return appLockEnabled;
    }

    public String getHelpCenterEmail() {
        return mContext.getString(R.string.const_support_help_center_email);
    }

    public String getHelpCenterSubject() {
        return mContext.getString(R.string.app_name) + " - Android";
    }

    public String getHelpCenterBody() {
        return "Version : " + App.getVersion() + "\n"
                + "Platform : Android " + Build.VERSION.RELEASE + "\n"
                + "Device : " + Build.BRAND.toUpperCase() + " " + Build.MODEL.toUpperCase() + "\n"
                + "Account : " + AccountModel.getAccount() + "\n\n"
                + mContext.getString(R.string.settings_help_center_msg) + " : \n";
    }

    public MutableLiveData<Result> getLogoutResult() {
        return logoutResult;
    }


    /*
        action
     */

    public void logout() {
        disposable = AccountModel.logout().subscribe(
                () -> logoutResult.postValue(new Result()),
                throwable -> logoutResult.postValue(new Result(throwable))
        );
    }
}

