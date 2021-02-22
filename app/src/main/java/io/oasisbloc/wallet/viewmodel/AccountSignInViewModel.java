package io.oasisbloc.wallet.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.model.AccountModel;

public class AccountSignInViewModel extends BaseViewModel {

    private String mAccount;
    private String mPassword;

    private MutableLiveData<Boolean> inputValidate = new MutableLiveData<>();
    private MutableLiveData<Result> signInResult = new MutableLiveData<>();

    public static AccountSignInViewModel get(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AccountSignInViewModel();
            }
        }).get(AccountSignInViewModel.class);
    }

    private AccountSignInViewModel() {
        inputValidate.setValue(false);
    }


    /*
        setter
     */

    public void setInput(String account, String password) {
        boolean accountValidate = PolicyUtils.isAccountValidate(mAccount = account);
        boolean passwordValidate = PolicyUtils.isPasswordValidate(mPassword = password);
        inputValidate.postValue(accountValidate && passwordValidate);
    }


    /*
        getter
     */

    public MutableLiveData<Boolean> getInputValidate() {
        return inputValidate;
    }

    public MutableLiveData<Result> getSignInResult() {
        return signInResult;
    }


    /*
         action
    */

    public void signIn() {
        disposable = AccountModel.signInAccount(mAccount, mPassword).subscribe(
                () -> signInResult.postValue(new Result()),
                throwable -> signInResult.postValue(new Result(throwable))
        );
    }
}
