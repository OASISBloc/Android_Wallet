package io.oasisbloc.wallet.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.databinding.ActivityAccountSignInBinding;
import io.oasisbloc.wallet.ui.modal.AdvanceInputView;
import io.oasisbloc.wallet.ui.setting.ResetPasswordActivity;
import io.oasisbloc.wallet.ui.splash.SplashActivity;
import io.oasisbloc.wallet.viewmodel.AccountSignInViewModel;

public class AccountSignInActivity extends BaseActivity {

    private ActivityAccountSignInBinding mBinding;
    private AccountSignInViewModel mViewModel;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountSignInActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarBackButtonEnable(R.drawable.ic_action_bar_close);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_sign_in);
        mViewModel = AccountSignInViewModel.get(this);

        mBinding.account.setType(AdvanceInputView.Type.ACCOUNT);
        mBinding.account.setLabel(R.string.account_label_account);
        mBinding.account.setTextHint(R.string.account_hint_account);
        mBinding.account.addTextChangedListener(createInputWatcher());
        mBinding.account.setError(R.string.account_error_account);
        mBinding.account.setVisibilityEnabled(false);
        mBinding.account.requestFocusAndShowKeyboard();

        mBinding.password.setType(AdvanceInputView.Type.PASSWORD);
        mBinding.password.setLabel(R.string.account_label_password);
        mBinding.password.setTextHint(R.string.account_hint_password);
        mBinding.password.addTextChangedListener(createInputWatcher());
        mBinding.password.setError(R.string.account_error_password);

        mBinding.actionFind.setOnClickListener(v -> onFindPasswordClick());
        mBinding.actionSignIn.setOnClickListener(v -> onSignInClicked());
        mViewModel.getInputValidate().observe(this, mBinding.actionSignIn::setEnabled);
        mViewModel.getSignInResult().observe(this, this::handleSignInResult);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_bottom);
    }

    private TextWatcher createInputWatcher() {
        return ViewUtils.createSimpleTextWatcher(data -> mViewModel.setInput(
                mBinding.account.getText(),
                mBinding.password.getText()));
    }

    private void onFindPasswordClick() {
        ViewUtils.hideKeyboard(this);
        ResetPasswordActivity.start(this, getString(R.string.account_action_forgot_password));
    }

    private void onSignInClicked() {
        ViewUtils.hideKeyboard(this);
        showProgressDialog();
        mViewModel.signIn();
    }

    private void handleSignInResult(Result result) {
        dismissProgressDialog();
        if (result.isSuccessful()) {
            SplashActivity.start(this);
        }

        if (result.isFailure()) {
            mBinding.password.setErrorForce(ExceptionUtils.getMessage(result.getError()));
        }
    }
}
