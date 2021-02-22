package io.oasisbloc.wallet.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.databinding.ActivityResetPasswordBinding;
import io.oasisbloc.wallet.ui.modal.AdvanceInputView;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.setting.viewmodel.ResetPasswordViewModel;
import io.oasisbloc.wallet.ui.splash.SplashActivity;

public class ResetPasswordActivity extends BaseActivity {

    private static final String KEY_TITLE = "TITLE";

    private ActivityResetPasswordBinding mBinding;
    private ResetPasswordViewModel mViewModel;

    public static void start(Context context, String title) {
        Intent starter = new Intent(context, ResetPasswordActivity.class);
        starter.putExtra(KEY_TITLE, title);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBarBackButtonEnable();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        mViewModel = ResetPasswordViewModel.get(this);

        mBinding.title.setText(getIntent().getStringExtra(KEY_TITLE));

        mBinding.account.setType(AdvanceInputView.Type.ACCOUNT);
        mBinding.account.setLabel(R.string.settings_reset_password_account_label);
        mBinding.account.addTextChangedListener(createInputWatcher());
        mBinding.account.setTextHint(R.string.account_hint_account);
        mBinding.account.setError(R.string.account_error_account);
        mBinding.account.setVisibilityEnabled(false);
        mBinding.account.requestFocusAndShowKeyboard();

        mBinding.email.setType(AdvanceInputView.Type.EMAIL);
        mBinding.email.setLabel(R.string.settings_reset_password_email_label);
        mBinding.email.addTextChangedListener(createInputWatcher());
        mBinding.email.setTextHint(R.string.account_hint_email);
        mBinding.email.setError(R.string.account_error_email);
        mBinding.email.setVisibilityEnabled(false);

        mBinding.action.setOnClickListener(v -> onSendClicked());
        mViewModel.getInputValidateLiveData().observe(this, mBinding.action::setEnabled);
        mViewModel.getEmailSendingLiveDate().observe(
                this,
                this::observeEmailSendingSuccess,
                e -> mBinding.email.setErrorForce(ExceptionUtils.getMessage(e)),
                this::handleProgressDialog);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }


    private TextWatcher createInputWatcher() {
        return ViewUtils.createSimpleTextWatcher(data ->
                mViewModel.setInput(mBinding.account.getText(), mBinding.email.getText())
        );
    }

    private void onSendClicked() {
        ViewUtils.hideKeyboard(this);
        mViewModel.sendEmail();
    }

    private void observeEmailSendingSuccess() {
        new CenterModal(this)
                .setCancelable(false)
                .setTitle(R.string.settings_reset_password_done_title)
                .setMessage(R.string.settings_reset_password_done_msg)
                .setPositiveButton(R.string.action_ok, (d, w) -> SplashActivity.start(this))
                .show();
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}
