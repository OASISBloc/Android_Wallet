package io.oasisbloc.wallet.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.databinding.ActivityAccountCreateBinding;
import io.oasisbloc.wallet.ui.modal.AdvanceInputView;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.viewmodel.AccountCreateViewModel;

public class AccountCreateActivity extends BaseActivity {

    private ActivityAccountCreateBinding mBinding;
    private AccountCreateViewModel mViewModel;

    private TextWatcher mPasswordTextWatcher;
    private TextWatcher mEmailTextWatcher;
    private CompoundButton.OnCheckedChangeListener mAgreeCheckWatcher;
    private ViewTreeObserver.OnGlobalLayoutListener mKeyboardListener;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountCreateActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarBackButtonEnable(R.drawable.ic_action_bar_close);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_create);
        mViewModel = AccountCreateViewModel.get(this);

        initAccountView();
        initPasswordView();
        initKeypairView();
        initEmailView();

        mViewModel.getAccountInputError().observe(this, mBinding.account.input::setErrorForce);
        mViewModel.getInputValidate().observe(this, mBinding.action::setEnabled);
        mViewModel.getStepResult().observe(this, this::observeStepResult);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {
        new CenterModal(this)
                .setCancelable(false)
                .setTitle(R.string.account_title_create_cancel)
                .setMessage(R.string.account_msg_create_cancel)
                .setNegativeButton(R.string.action_cancel, (d, w) -> super.onBackPressed())
                .setPositiveButton(R.string.action_continue, null)
                .show();
    }


    /*
        account
     */

    private void initAccountView() {
        TextWatcher accountTextWatcher = ViewUtils.createSimpleTextWatcher(data ->
                mViewModel.setAccount(mBinding.account.input.getText())
        );

        mBinding.account.input.setType(AdvanceInputView.Type.ACCOUNT);
        mBinding.account.input.setLabel(R.string.account_label_account);
        mBinding.account.input.setTextHint(R.string.account_hint_account);
        mBinding.account.input.addTextChangedListener(accountTextWatcher);
        mBinding.account.input.setError(R.string.account_error_account);
        mBinding.account.input.setVisibilityEnabled(false);
        mBinding.account.input.requestFocusAndShowKeyboard();
    }

    private void onAccountActionClicked() {
        ViewUtils.hideKeyboard(this, mBinding.root);
        showProgressDialog();
        mViewModel.checkAccount();
    }


    /*
        password
     */

    private void initPasswordView() {
        mPasswordTextWatcher = ViewUtils.createSimpleTextWatcher(data ->
                mViewModel.setPassword(
                        mBinding.password.input1.getText(),
                        mBinding.password.input2.getText())
        );

        mBinding.password.input1.setType(AdvanceInputView.Type.PASSWORD);
        mBinding.password.input1.setLabel(R.string.account_label_password);
        mBinding.password.input1.setTextHint(R.string.account_hint_password);
        mBinding.password.input1.addTextChangedListener(mPasswordTextWatcher);
        mBinding.password.input1.setError(R.string.account_error_password);

        mBinding.password.input2.setSource(mBinding.password.input1);
        mBinding.password.input2.setType(AdvanceInputView.Type.RE_INPUT);
        mBinding.password.input2.setLabel(R.string.account_label_password_re);
        mBinding.password.input2.setTextHint(R.string.account_hint_password_re);
        mBinding.password.input2.addTextChangedListener(mPasswordTextWatcher);
        mBinding.password.input2.setError(R.string.account_error_password_re);
    }

    private void clearPasswordView() {
        mBinding.password.input1.removeTextChangedListener(mPasswordTextWatcher);
        mBinding.password.input1.setText("");
        mBinding.password.input1.addTextChangedListener(mPasswordTextWatcher);

        mBinding.password.input2.removeTextChangedListener(mPasswordTextWatcher);
        mBinding.password.input2.setText("");
        mBinding.password.input2.addTextChangedListener(mPasswordTextWatcher);
    }

    private void onPasswordActionClicked() {
        ViewUtils.hideKeyboard(this, mBinding.root);
        showProgressDialog();
        mViewModel.checkPassword();
    }


    /*
        keypair
     */

    private void initKeypairView() {
        mBinding.keypair.generate.setOnClickListener(v -> onGenerateKeyPairClicked());
        ViewUtils.setAsterisk(mBinding.keypair.privateKey);
        ViewUtils.setToggleAsteriskListener(mBinding.keypair.privateKeyVisibility, mBinding.keypair.privateKey);
        mBinding.keypair.copy.setOnClickListener(v -> onKeyPairCopyClicked());
        mBinding.keypair.copy.setEnabled(false);
        mViewModel.getPublicKey().observe(this, this::observePublicKey);
        mViewModel.getPrivateKey().observe(this, this::observePrivateKey);
    }

    private void clearKeyPairView() {
        mBinding.keypair.publicKey.setText("");
        mBinding.keypair.privateKey.setText("");
        mBinding.keypair.copy.setEnabled(false);
    }

    private void onGenerateKeyPairClicked() {
        ViewUtils.hideKeyboard(this, mBinding.root);
        showProgressDialog();
        mViewModel.generateKeyPair();
    }

    private void onKeyPairCopyClicked() {
        ViewUtils.copyToClipboard(this, mViewModel.getBackupText());
        new CenterModal(this)
                .setCancelable(false)
                .setTitle(R.string.account_title_create_copy)
                .setMessage(R.string.account_msg_create_copy)
                .setPositiveButton(R.string.action_ok, null)
                .show();
    }

    private void onKeyPairActionClicked() {
        ViewUtils.hideKeyboard(this, mBinding.root);
        showProgressDialog();
        mViewModel.checkKeyPair();
    }

    private void observePublicKey(String key) {
        dismissProgressDialog();
        mBinding.keypair.publicKey.setText(key);
        mBinding.keypair.copy.setEnabled(true);
    }

    private void observePrivateKey(String key) {
        dismissProgressDialog();
        mBinding.keypair.privateKey.setText(key);
        mBinding.keypair.copy.setEnabled(true);
    }


    /*
        email
     */

    private void initEmailView() {
        mEmailTextWatcher = ViewUtils.createSimpleTextWatcher(data ->
                mViewModel.setEmail(mBinding.email.input.getText())
        );
        mAgreeCheckWatcher = (buttonView, isChecked) -> {
            ViewUtils.hideKeyboard(this, mBinding.root);
            mViewModel.setAgreement(
                    mBinding.email.agreement1.isChecked(),
                    mBinding.email.agreement2.isChecked()
            );
        };

        mBinding.email.input.setType(AdvanceInputView.Type.EMAIL);
        mBinding.email.input.setLabel(R.string.account_label_email);
        mBinding.email.input.setTextHint(R.string.account_hint_email);
        mBinding.email.input.addTextChangedListener(mEmailTextWatcher);
        mBinding.email.input.setError(R.string.account_error_email);
        mBinding.email.input.setVisibilityEnabled(false);

        mBinding.email.agreement1.setOnCheckedChangeListener(mAgreeCheckWatcher);
        mBinding.email.agreement2.setOnCheckedChangeListener(mAgreeCheckWatcher);
    }

    private void clearEmailView() {
        mBinding.email.input.removeTextChangedListener(mEmailTextWatcher);
        mBinding.email.input.setText("");
        mBinding.email.input.addTextChangedListener(mEmailTextWatcher);

        mBinding.email.agreement1.setOnCheckedChangeListener(null);
        mBinding.email.agreement1.setChecked(false);
        mBinding.email.agreement1.setOnCheckedChangeListener(mAgreeCheckWatcher);

        mBinding.email.agreement2.setOnCheckedChangeListener(null);
        mBinding.email.agreement2.setChecked(false);
        mBinding.email.agreement2.setOnCheckedChangeListener(mAgreeCheckWatcher);
    }



    /*
        action
     */

    private void onCreateActionClicked() {
        ViewUtils.hideKeyboard(this, mBinding.root);
        new CenterModal(this)
                .setCancelable(false)
                .setTitle(R.string.account_title_create_check)
                .setMessage(R.string.account_msg_create_check)
                .setNegativeButton(R.string.account_action_copy_cancel, null)
                .setPositiveButton(R.string.account_action_copy_done, (dialog, which) -> {
                    ViewUtils.hideKeyboard(this, mBinding.root);
                    showProgressDialog();
                    mViewModel.createAccount();
                })
                .show();
    }

    private void observeStepResult(Result<AccountCreateStep> result) {
        dismissProgressDialog();
        if (result.isSuccessful()) {
            observeStep(result.getResult());
        }
    }

    private void observeStep(AccountCreateStep step) {
        switch (step) {
            case ACCOUNT:
                mBinding.account.input.requestFocusAndShowKeyboard();
                mBinding.account.getRoot().setVisibility(View.VISIBLE);
                mBinding.password.getRoot().setVisibility(View.GONE);
                mBinding.keypair.getRoot().setVisibility(View.GONE);
                mBinding.email.getRoot().setVisibility(View.GONE);
                mBinding.action.setText(R.string.action_next);
                mBinding.action.setOnClickListener(v -> onAccountActionClicked());
                clearPasswordView();
                clearKeyPairView();
                clearEmailView();
                break;
            case PASSWORD:
                mBinding.password.input1.requestFocusAndShowKeyboard();
                mBinding.account.getRoot().setVisibility(View.VISIBLE);
                mBinding.password.getRoot().setVisibility(View.VISIBLE);
                mBinding.keypair.getRoot().setVisibility(View.GONE);
                mBinding.email.getRoot().setVisibility(View.GONE);
                mBinding.action.setText(R.string.action_next);
                mBinding.action.setOnClickListener(v -> onPasswordActionClicked());
                clearKeyPairView();
                clearEmailView();
                break;
            case KEY_PAIR:
                mBinding.account.getRoot().setVisibility(View.VISIBLE);
                mBinding.password.getRoot().setVisibility(View.VISIBLE);
                mBinding.keypair.getRoot().setVisibility(View.VISIBLE);
                mBinding.email.getRoot().setVisibility(View.GONE);
                mBinding.action.setText(R.string.action_next);
                mBinding.action.setOnClickListener(v -> onKeyPairActionClicked());
                clearEmailView();
                break;
            case EMAIL:
                mKeyboardListener = ViewUtils.addOnShowKeyboardListener(this, mBinding.root, data -> {
                    if (data) {
                        int offset = mBinding.email.getRoot().getTop();
                        mBinding.scrollView.scrollTo(0, offset);
                        mBinding.email.input.requestFocusAndShowKeyboard();
                        ViewUtils.removeOnShowKeyboardListener(mBinding.root, mKeyboardListener);
                    }
                });
                ViewUtils.showKeyboard(mBinding.root);
                mBinding.account.getRoot().setVisibility(View.VISIBLE);
                mBinding.password.getRoot().setVisibility(View.VISIBLE);
                mBinding.keypair.getRoot().setVisibility(View.VISIBLE);
                mBinding.email.getRoot().setVisibility(View.VISIBLE);
                mBinding.action.setText(R.string.account_action_create_account);
                mBinding.action.setOnClickListener(v -> onCreateActionClicked());
                break;
            case DONE:
                AccountDoneActivity.start(this, mBinding.account.input.getText());
                finish();
                break;
        }
    }
}