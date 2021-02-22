package io.oasisbloc.wallet.ui.setting.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.base.livedata.CompletableLiveData;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.model.repository.remote.RemoteException;

public class KeyBackupViewModel extends BaseViewModel {

    private Context mContext;
    private String mPassword;

    private MutableLiveData<String> publicKey = new MutableLiveData<>();
    private MutableLiveData<String> privateKey = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordValidate = new MutableLiveData<>();
    private MutableLiveData<String> passwordInputError = new MutableLiveData<>();
    private CompletableLiveData passwordChecking = new CompletableLiveData();

    public static KeyBackupViewModel get(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new KeyBackupViewModel(activity);
            }
        }).get(KeyBackupViewModel.class);
    }

    private KeyBackupViewModel(Context context) {
        mContext = context;
        publicKey.setValue(AccountModel.getPublicKey());
        privateKey.setValue(AccountModel.getPrivateKey());
        passwordValidate.setValue(false);
    }


    /*
        setter
     */

    public void setPassword(String password) {
        boolean validate = PolicyUtils.isPasswordValidate(mPassword = password);
        passwordValidate.postValue(validate);
    }


    /*
        getter
     */

    public MutableLiveData<Boolean> getInputValidateLiveDate() {
        return passwordValidate;
    }

    public CompletableLiveData getPasswordCheckingLiveData() {
        return passwordChecking;
    }

    public MutableLiveData<String> getPasswordInputError() {
        return passwordInputError;
    }

    public MutableLiveData<String> getPublicKeyLiveDate() {
        return publicKey;
    }

    public MutableLiveData<String> getPrivateKeyLiveDate() {
        return privateKey;
    }

    public String getBackupText() {
        return getString(mContext, R.string.account_label_account) +
                " : " +
                AccountModel.getAccount() +
                "\n" +
                getString(mContext, R.string.account_label_public_key) +
                " : " +
                AccountModel.getPublicKey() +
                "\n" +
                getString(mContext, R.string.account_label_private_key) +
                " : " +
                AccountModel.getPrivateKey();
    }


    /*
         action
    */

    public void checkPassword() {
        passwordChecking.setLoading(true);
        disposable = AccountModel.checkPassword(mPassword).subscribe(
                () -> {
                    passwordChecking.setSuccess();
                    passwordChecking.setLoading(false);
                    publicKey.postValue(AccountModel.getPublicKey());
                    privateKey.postValue(AccountModel.getPrivateKey());
                },
                throwable -> {
                    if (throwable instanceof RemoteException) {
                        passwordInputError.postValue(mContext.getString(R.string.settings_key_backup_password_error));
                    }
                    passwordChecking.setFailure(throwable);
                    passwordChecking.setLoading(false);
                }
        );
    }
}
