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

public class AccountImportViewModel extends BaseViewModel {

    private String mAccount;
    private String mPrivateKey;

    private MutableLiveData<Boolean> inputValidate = new MutableLiveData<>();
    private MutableLiveData<Result> importingResult = new MutableLiveData<>();

    public static AccountImportViewModel get(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AccountImportViewModel();
            }
        }).get(AccountImportViewModel.class);
    }

    private AccountImportViewModel() {
        inputValidate.setValue(false);
    }


    /*
        setter
     */

    public void setInput(String account, String privateKey) {
        boolean accountValidate = PolicyUtils.isAccountValidate(mAccount = account);
        boolean privateKeyValidate = PolicyUtils.isPrivateKeyValidate(mPrivateKey = privateKey);
        inputValidate.postValue(accountValidate && privateKeyValidate);
    }


    /*
        getter
     */

    public MutableLiveData<Boolean> getInputValidate() {
        return inputValidate;
    }

    public MutableLiveData<Result> getImportingResult() {
        return importingResult;
    }


    /*
        action
     */

    public void importing() {
        disposable = AccountModel.importAccount(mAccount, mPrivateKey).subscribe(
                () -> importingResult.postValue(new Result()),
                throwable -> importingResult.postValue(new Result(throwable))
        );
    }
}
