package io.oasisbloc.wallet.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.ui.account.AccountCreateStep;

public class AccountCreateViewModel extends BaseViewModel {

    private Context mContext;
    private AccountCreateStep mStep = AccountCreateStep.ACCOUNT;
    private String mAccount = "";
    private String mPassword1 = "";
    private String mPassword2 = "";
    private String mPublicKey = "";
    private String mPrivateKey = "";
    private String mEmail = "";
    private boolean mAgree1 = false;
    private boolean mAgree2 = false;

    private MutableLiveData<String> accountInputError = new MutableLiveData<>();
    private MutableLiveData<String> publicKey = new MutableLiveData<>();
    private MutableLiveData<String> privateKey = new MutableLiveData<>();
    private MutableLiveData<Boolean> inputValidate = new MutableLiveData<>();
    private MutableLiveData<Result<AccountCreateStep>> result = new MutableLiveData<>();

    public static AccountCreateViewModel get(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AccountCreateViewModel(activity);
            }
        }).get(AccountCreateViewModel.class);
    }

    private AccountCreateViewModel(Context context) {
        mContext = context;
        result.setValue(new Result<>(mStep));
        checkAvailableInput();
    }

    private void setStep(AccountCreateStep step) {
        if (mStep != step) {
            mStep = step;
            result.postValue(new Result<>(mStep));
        }
    }

    private void checkAvailableInput() {
        boolean accountValidate = PolicyUtils.isAccountValidate(mAccount);
        boolean passwordValidate1 = PolicyUtils.isPasswordValidate(mPassword1);
        boolean passwordValidate2 = mPassword1.equals(mPassword2);
        boolean keypairValidate = !TextUtils.isEmpty(mPublicKey) && !TextUtils.isEmpty(mPrivateKey);
        boolean emailValidate = PolicyUtils.isEmailValidate(mEmail);
        boolean agreementValidate = mAgree1 && mAgree2;

        switch (mStep) {
            case ACCOUNT:
                inputValidate.postValue(accountValidate);
                break;

            case PASSWORD:
                inputValidate.postValue(accountValidate &&
                        passwordValidate1
                        && passwordValidate2);
                break;

            case KEY_PAIR:
                inputValidate.postValue(accountValidate
                        && passwordValidate1
                        && passwordValidate2
                        && keypairValidate);
                break;

            case EMAIL:
                inputValidate.postValue(accountValidate
                        && passwordValidate1
                        && passwordValidate2
                        && keypairValidate
                        && emailValidate
                        && agreementValidate);
                break;
        }
    }


    /*
        action
     */

    public void checkAccount() {
        disposable = AccountModel.checkAccount(mAccount).subscribe(
                () -> {
                    setStep(AccountCreateStep.PASSWORD);
                    checkAvailableInput();
                },
                throwable -> {
                    result.postValue(new Result<>(throwable));
                    accountInputError.postValue(ExceptionUtils.getMessage(throwable));
                }
        );
    }

    public void checkPassword() {
        setStep(AccountCreateStep.KEY_PAIR);
        checkAvailableInput();
    }

    public void generateKeyPair() {
        disposable = AccountModel.createKeyPair().subscribe(
                keyPair -> {
                    mPublicKey = keyPair.getPublicKey();
                    mPrivateKey = keyPair.getPrivateKey();
                    mEmail = "";
                    mAgree1 = false;
                    mAgree2 = false;
                    publicKey.postValue(mPublicKey);
                    privateKey.postValue(mPrivateKey);
                    setStep(AccountCreateStep.KEY_PAIR);
                    checkAvailableInput();
                },
                throwable -> result.postValue(new Result<>(throwable))
        );
    }

    public void checkKeyPair() {
        setStep(AccountCreateStep.EMAIL);
        checkAvailableInput();
    }

    public void createAccount() {
        disposable = AccountModel
                .createAccount(mAccount,
                        mPassword1,
                        mPublicKey,
                        mEmail,
                        mAgree1,
                        mAgree2)
                .subscribe(
                        () -> setStep(AccountCreateStep.DONE),
                        throwable -> result.postValue(new Result<>(throwable))
                );
    }


    /*
        setter
     */

    public void setAccount(String account) {
        mAccount = account;
        mPassword1 = "";
        mPassword2 = "";
        mPublicKey = "";
        mPrivateKey = "";
        mEmail = "";
        mAgree1 = false;
        mAgree2 = false;
        setStep(AccountCreateStep.ACCOUNT);
        checkAvailableInput();
    }

    public void setPassword(String password1, String password2) {
        mPassword1 = password1;
        mPassword2 = password2;
        mPublicKey = "";
        mPrivateKey = "";
        mEmail = "";
        mAgree1 = false;
        mAgree2 = false;
        setStep(AccountCreateStep.PASSWORD);
        checkAvailableInput();
    }

    public void setEmail(String email) {
        mEmail = email;
        setStep(AccountCreateStep.EMAIL);
        checkAvailableInput();
    }

    public void setAgreement(boolean agree1, boolean agree2) {
        mAgree1 = agree1;
        mAgree2 = agree2;
        setStep(AccountCreateStep.EMAIL);
        checkAvailableInput();
    }


    /*
        getter
     */

    public MutableLiveData<String> getAccountInputError() {
        return accountInputError;
    }

    public MutableLiveData<String> getPublicKey() {
        return publicKey;
    }

    public MutableLiveData<String> getPrivateKey() {
        return privateKey;
    }

    public String getBackupText() {
        return getString(mContext, R.string.account_label_account) +
                " : " +
                mAccount +
                "\n" +
                getString(mContext, R.string.account_label_public_key) +
                " : " +
                mPublicKey +
                "\n" +
                getString(mContext, R.string.account_label_private_key) +
                " : " +
                mPrivateKey;
    }

    public MutableLiveData<Boolean> getInputValidate() {
        return inputValidate;
    }

    public MutableLiveData<Result<AccountCreateStep>> getStepResult() {
        return result;
    }
}

