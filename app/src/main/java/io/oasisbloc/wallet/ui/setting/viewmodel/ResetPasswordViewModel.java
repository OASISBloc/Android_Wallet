package io.oasisbloc.wallet.ui.setting.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.base.livedata.CompletableLiveData;
import io.oasisbloc.wallet.model.AccountModel;

public class ResetPasswordViewModel extends BaseViewModel {

    private String mAccount;
    private String mEmail;

    private MutableLiveData<Boolean> inputValidate = new MutableLiveData<>();
    private CompletableLiveData emailSending = new CompletableLiveData();

    public static ResetPasswordViewModel get(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ResetPasswordViewModel();
            }
        }).get(ResetPasswordViewModel.class);
    }

    private ResetPasswordViewModel() {
        inputValidate.setValue(false);
    }


    /*
        setter
     */

    public void setInput(String account, String email) {
        boolean accountValidate = PolicyUtils.isAccountValidate(mAccount = account);
        boolean emailValidate = PolicyUtils.isEmailValidate(mEmail = email);
        inputValidate.postValue(accountValidate && emailValidate);
    }


    /*
        getter
     */

    public MutableLiveData<Boolean> getInputValidateLiveData() {
        return inputValidate;
    }

    public CompletableLiveData getEmailSendingLiveDate() {
        return emailSending;
    }


    /*
         action
    */

    public void sendEmail() {
        emailSending.setLoading(true);
        disposable = AccountModel
                .sendPasswordResettingEmail(mAccount, mEmail)
                .andThen(AccountModel.logout())
                .subscribe(
                        () -> {
                            emailSending.setSuccess();
                            emailSending.setLoading(false);
                        },
                        throwable -> {
                            emailSending.setFailure(throwable);
                            emailSending.setLoading(false);
                        }
                );
    }
}
