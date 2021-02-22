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
import io.oasisbloc.wallet.databinding.ActivityAccountImportBinding;
import io.oasisbloc.wallet.ui.modal.AdvanceInputView;
import io.oasisbloc.wallet.ui.splash.SplashActivity;
import io.oasisbloc.wallet.viewmodel.AccountImportViewModel;

public class AccountImportActivity extends BaseActivity {

    private ActivityAccountImportBinding mBinding;
    private AccountImportViewModel mViewModel;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountImportActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarBackButtonEnable(R.drawable.ic_action_bar_close);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_import);
        mViewModel = AccountImportViewModel.get(this);

        mBinding.account.setType(AdvanceInputView.Type.ACCOUNT);
        mBinding.account.setLabel(R.string.account_label_account);
        mBinding.account.setTextHint(R.string.account_hint_account);
        mBinding.account.addTextChangedListener(createInputWatcher());
        mBinding.account.setError(R.string.account_error_account);
        mBinding.account.setVisibilityEnabled(false);
        mBinding.account.requestFocusAndShowKeyboard();

        mBinding.privateKey.setType(AdvanceInputView.Type.PRIVATE_KRY);
        mBinding.privateKey.setLabel(R.string.account_label_private_key);
        mBinding.privateKey.setTextHint(R.string.account_hint_private_key);
        mBinding.privateKey.addTextChangedListener(createInputWatcher());
        mBinding.privateKey.setError(R.string.account_error_private_key);

        mBinding.actionImport.setOnClickListener(v -> onImportClicked());
        mViewModel.getInputValidate().observe(this, mBinding.actionImport::setEnabled);
        mViewModel.getImportingResult().observe(this, this::handleImporting);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_bottom);
    }

    private TextWatcher createInputWatcher() {
        return ViewUtils.createSimpleTextWatcher(data -> mViewModel.setInput(
                mBinding.account.getText(),
                mBinding.privateKey.getText()));
    }

    private void onImportClicked() {
        ViewUtils.hideKeyboard(this);
        showProgressDialog();
        mViewModel.importing();
    }

    private void handleImporting(Result result) {
        dismissProgressDialog();
        if (result.isSuccessful()) {
            SplashActivity.start(this);
        }

        if (result.isFailure()) {
            mBinding.privateKey.setErrorForce(ExceptionUtils.getMessage(result.getError()));
        }
    }
}
