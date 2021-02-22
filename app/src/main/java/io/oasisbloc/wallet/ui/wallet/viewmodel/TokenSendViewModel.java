package io.oasisbloc.wallet.ui.wallet.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.DecimalUtils;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.base.livedata.CompletableLiveData;
import io.oasisbloc.wallet.base.livedata.SingleLiveData;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.model.WalletModel;

public class TokenSendViewModel extends BaseViewModel {

    private Context mContext;
    private String mSymbol;
    private double mBalanceAmount;
    private TokenSendStep mStep;

    private double mInputAmount = 0;
    private String mInputAccount = "";
    private String mInputMemo = "";
    private String mToken = "";
    private String mInputPrivateKey = "";
    private String mInputPassword = "";

    private MutableLiveData<String> amountError = new MutableLiveData<>();
    private MutableLiveData<String> accountError = new MutableLiveData<>();
    private MutableLiveData<String> memoError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> privateKeyError = new MutableLiveData<>();

    private MutableLiveData<TokenSendStep> step = new MutableLiveData<>();
    private MutableLiveData<Boolean> inputValidate = new MutableLiveData<>();
    private SingleLiveData<String> balanceInfo = new SingleLiveData<>();
    private CompletableLiveData sendResult = new CompletableLiveData();

    public static TokenSendViewModel get(FragmentActivity activity, String symbol) {
        TokenSendViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TokenSendViewModel(activity, symbol);
            }
        }).get(TokenSendViewModel.class);
        activity.getLifecycle().addObserver(vm);
        return vm;
    }

    private TokenSendViewModel(Context context, String symbol) {
        mContext = context;
        mSymbol = symbol;
//        mBalanceAmount = 0;
        mBalanceAmount = 10000000;
        mStep = TokenSendStep.PRESET;

        amountError.setValue("");
        accountError.setValue("");
        memoError.setValue("");
        passwordError.setValue("");
        privateKeyError.setValue("");

        step.setValue(mStep);
        inputValidate.setValue(false);
    }

    @Override
    public void onStart() {
        super.onStart();
//        fetchBalanceInfo();  yskim test
    }

    private void setStep(TokenSendStep value) {
        if (mStep != value) {
            mStep = value;
            step.postValue(value);
        }
    }

    private void checkInputValidate() {
        boolean amountValidate = PolicyUtils.isAmountValidate(mInputAmount, mBalanceAmount);
        boolean accountValidate = PolicyUtils.isAccountValidate(mInputAccount);
        boolean memoValidate = PolicyUtils.isMemoValidate(mInputMemo);
        boolean passwordValidate = PolicyUtils.isPasswordValidate(mInputPassword);
        boolean privateKeyValidate = PolicyUtils.isPrivateKeyValidate(mInputPrivateKey);

        if (TokenSendStep.PRESET == mStep) {
            inputValidate.postValue(amountValidate
                    && accountValidate
                    && memoValidate);

        } else {
            inputValidate.postValue(amountValidate
                    && accountValidate
                    && memoValidate
                    && passwordValidate
                    && privateKeyValidate);
        }
    }

    private void fetchBalanceInfo() {
        balanceInfo.setLoading(true);
        disposable = WalletModel.getBalance().subscribe(
                result -> {
//                    mBalanceAmount = result;
//                    balanceInfo.setResult(mContext.getString(
//                            R.string.token_send_amount_info_s,
//                            DecimalUtils.to(result, mSymbol)),
//                            null);
//                    balanceInfo.setLoading(false);
                },
                throwable -> {
//                    balanceInfo.setResult(null, throwable);
//                    balanceInfo.setLoading(false);
                }
        );
    }


    /*
        setter
     */

    public void setAmount(String amount) {
        try {
            double amountValue = Double.parseDouble(amount);
            mInputAmount = amountValue;
            if (PolicyUtils.getAmountMinValue() > amountValue) {
                amountError.postValue(mContext.getString(R.string.token_send_amount_error_under));
            } else if (mBalanceAmount < amountValue) {
                amountError.postValue(mContext.getString(R.string.token_send_amount_error_over));
            } else {
                amountError.postValue("");
            }
        } catch (Exception e) {
            mInputAmount = 0;
            if (TextUtils.isEmpty(amount)) {
                amountError.postValue("");
            } else {
                amountError.postValue(ExceptionUtils.getMessage(e));
            }
        }
        checkInputValidate();
    }

    public void setAccount(String account) {
        mInputAccount = account;
        boolean validate = PolicyUtils.isAccountValidate(account);
        if (TextUtils.isEmpty(account) || validate) {
            accountError.postValue("");
        } else {
            accountError.postValue(mContext.getString(R.string.token_send_account_error));
        }
        checkInputValidate();
    }

    public void setMemo(String memo) {
        mInputMemo = memo;
        boolean validate = PolicyUtils.isMemoValidate(memo);
        if (TextUtils.isEmpty(memo) || validate) {
            memoError.postValue("");
        } else {
            memoError.postValue(mContext.getString(R.string.token_send_memo_error));
        }
        checkInputValidate();
    }

    public void setToken(String token){
        mToken = token;
    }

    public void setPassword(String password) {
        mInputPassword = password;
        boolean validate = PolicyUtils.isPasswordValidate(password);
        if (TextUtils.isEmpty(password) || validate) {
            passwordError.postValue("");
        } else {
            passwordError.postValue(mContext.getString(R.string.token_send_password_error));
        }
        checkInputValidate();
    }

    public void setPrivateKey(String key) {
        mInputPrivateKey = key;
        boolean validate = PolicyUtils.isPrivateKeyValidate(key);
        if (TextUtils.isEmpty(key) || validate) {
            privateKeyError.postValue("");
        } else {
            privateKeyError.postValue(mContext.getString(R.string.token_send_private_key_error));
        }
        checkInputValidate();
    }


    /*
        getter
     */

    public MutableLiveData<String> getAmountErrorLiveData() {
        return amountError;
    }

    public MutableLiveData<String> getAccountErrorLiveData() {
        return accountError;
    }

    public MutableLiveData<String> getMemoErrorLiveData() {
        return memoError;
    }

    public MutableLiveData<String> getPasswordErrorLiveData() {
        return passwordError;
    }

    public MutableLiveData<String> getPrivateKeyErrorLiveData() {
        return privateKeyError;
    }

    public MutableLiveData<TokenSendStep> getStepLiveData() {
        return step;
    }

    public MutableLiveData<Boolean> getInputValidateLiveDate() {
        return inputValidate;
    }

    public SingleLiveData<String> getBalanceInfoLiveData() {
        return balanceInfo;
    }

    public CompletableLiveData getSendResultLiveData() {
        return sendResult;
    }

    public String getFromAccount() {
        return AccountModel.getAccount();
    }

    public String getToAccount() {
        return mInputAccount;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public String getAmount() {
        return DecimalUtils.to(mInputAmount);
    }


    /*
        action
     */

    public void actionNext() {
//        if (TextUtils.isEmpty(AccountModel.getPrivateKey())) {
            setStep(TokenSendStep.PRIVATE_KEY);
            setPassword(AccountModel.getPassword());
//        } else {
//            setStep(TokenSendStep.PASSWORD);
//            setPrivateKey(AccountModel.getPrivateKey());
//        }
    }

    public void actionSend() {
        sendResult.setLoading(true);
//        disposable = WalletModel.sendToken(
//                mInputAccount,
//                Double.toString(mInputAmount),
//                mInputMemo,
//                mInputPrivateKey,
//                mInputPassword)
//                .subscribe(
//                        () -> {
//                            sendResult.setSuccess();
//                            sendResult.setLoading(false);
//                        },
//                        throwable -> {
//                            sendResult.setFailure(throwable);
//                            sendResult.setLoading(false);
//                        }
//                );
        disposable = WalletModel.sendToken(
                mInputAccount,
                Double.toString(mInputAmount),
                mInputMemo,
                mInputPrivateKey,
                mToken
                )
                .subscribe(
                        () -> {
                            sendResult.setSuccess();
                            sendResult.setLoading(false);
                        },
                        throwable -> {
                            sendResult.setFailure(throwable);
                            sendResult.setLoading(false);
                        }
                );
    }
}
