package io.oasisbloc.wallet.ui.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.databinding.ActivityTokenSendBinding;
import io.oasisbloc.wallet.databinding.DialogTokenSendChekBinding;
import io.oasisbloc.wallet.databinding.DialogTokenSendFailureBinding;
import io.oasisbloc.wallet.databinding.DialogTokenSendSuccessfulBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.wallet.viewmodel.TokenSendStep;
import io.oasisbloc.wallet.ui.wallet.viewmodel.TokenSendViewModel;

public class TokenSendActivity extends BaseActivity {

    private static final String KEY_SYMBOL = "SYMBOL";
    private static final String KEY_USER = "USER";

    private ActivityTokenSendBinding mBinding;
    private TokenSendViewModel mViewModel;

    private String mUserId;
    private static String mSymbol;

    public static void start(Context context, String symbol, String userid) {

        mSymbol = symbol;

        Intent starter = new Intent(context, TokenSendActivity.class);
        starter.putExtra(KEY_SYMBOL, symbol);
        starter.putExtra(KEY_USER, userid);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBarBackButtonEnable();

        mViewModel = TokenSendViewModel.get(this, getIntent().getStringExtra(KEY_SYMBOL));
        mUserId = getIntent().getStringExtra(KEY_USER);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_token_send);

        //token
        mViewModel.setToken(mSymbol);

        // amount
        ViewUtils.addTextChangedListener(mBinding.amountValue, mViewModel::setAmount);
        ViewUtils.initAmountEditText(mBinding.amountValue);
        observeStringEmptyHide(mViewModel.getAmountErrorLiveData(), mBinding.amountError);
        mBinding.amountClear.setOnClickListener(v -> mBinding.amountValue.setText(""));

        // account
        if(mUserId.length() > 0){
            mBinding.accountValue.setText(mUserId);
            mViewModel.setAccount(mUserId);
        }
        ViewUtils.addTextChangedListener(mBinding.accountValue, mViewModel::setAccount);
        observeStringEmptyHide(mViewModel.getAccountErrorLiveData(), mBinding.accountError);
        mBinding.accountClear.setOnClickListener(v -> mBinding.accountValue.setText(""));

        // memo
        ViewUtils.addTextChangedListener(mBinding.memoValue, mViewModel::setMemo);
        observeStringEmptyHide(mViewModel.getMemoErrorLiveData(), mBinding.memoError);
        mBinding.memoClear.setOnClickListener(v -> mBinding.memoValue.setText(""));

        // password
        ViewUtils.setAsterisk(mBinding.passwordValue);
        ViewUtils.addTextChangedListener(mBinding.passwordValue, mViewModel::setPassword);
        observeStringEmptyHide(mViewModel.getPasswordErrorLiveData(), mBinding.passwordError);
        ViewUtils.setToggleAsteriskListener(mBinding.passwordHide, mBinding.passwordValue);
        mBinding.passwordClear.setOnClickListener(v -> mBinding.passwordValue.setText(""));

        // private key
        ViewUtils.setAsterisk(mBinding.privateKeyValue);
        ViewUtils.addTextChangedListener(mBinding.privateKeyValue, mViewModel::setPrivateKey);
        observeStringEmptyHide(mViewModel.getPrivateKeyErrorLiveData(), mBinding.privateKeyError);
        ViewUtils.setToggleAsteriskListener(mBinding.privateKeyHide, mBinding.privateKeyValue);
        mBinding.privateKeyClear.setOnClickListener(v -> mBinding.privateKeyValue.setText(""));

        // etc
        mViewModel.getStepLiveData().observe(this, this::observeType);
        mViewModel.getInputValidateLiveDate().observe(this, mBinding.action::setEnabled);
        mViewModel.getBalanceInfoLiveData().observe(
                this,
                mBinding.amountInfo::setText,
                this::showToastAndFinish,
                this::handleProgressDialog);
        mViewModel.getSendResultLiveData().observe(
                this,
                this::showSendSuccessfulDialog,
                this::showSendFailureDialog,
                this::handleProgressDialog
        );
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }

    private void observeStringEmptyHide(LiveData<String> data, TextView view) {
        data.observe(this, s -> {
            if (TextUtils.isEmpty(s)) {
                view.setVisibility(View.GONE);
            } else {
                view.setText(s);
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    private void observeType(TokenSendStep type) {
        switch (type) {
            case PRESET:
                mBinding.passwordLayout.setVisibility(View.GONE);
                mBinding.privateKeyLayout.setVisibility(View.GONE);
                mBinding.action.setText(R.string.action_next);
                mBinding.action.setOnClickListener(v -> onActionNextClicked());
                ViewUtils.showKeyboard(mBinding.amountValue);
                break;

            case PASSWORD:
                mBinding.passwordLayout.setVisibility(View.VISIBLE);
                mBinding.privateKeyLayout.setVisibility(View.GONE);
                mBinding.action.setText(R.string.action_send);
                mBinding.action.setOnClickListener(v -> onActionSendClicked());
                ViewUtils.showKeyboard(mBinding.passwordValue);
                break;

            case PRIVATE_KEY:
                mBinding.passwordLayout.setVisibility(View.GONE);
                mBinding.privateKeyLayout.setVisibility(View.VISIBLE);
                mBinding.action.setText(R.string.action_send);
                mBinding.action.setOnClickListener(v -> onActionSendClicked());
                ViewUtils.showKeyboard(mBinding.privateKeyValue);
                break;
        }
    }

    private void onActionNextClicked() {
        mViewModel.actionNext();
    }

    private void onActionSendClicked() {
        ViewUtils.hideKeyboard(this);
        DialogTokenSendChekBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_token_send_chek,
                null,
                false);

        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());

        binding.from.setText(mViewModel.getFromAccount());
        binding.to.setText(mViewModel.getToAccount());
//        binding.symbol.setText(mViewModel.getSymbol());
        binding.symbol.setText(mSymbol);
        binding.amount.setText(mViewModel.getAmount());
        binding.negative.setOnClickListener(v -> dialog.dismiss());
        binding.positive.setOnClickListener(v -> {
            dialog.dismiss();
            showProgressDialog();
            mViewModel.actionSend();
        });
    }

    private void showSendSuccessfulDialog() {
        DialogTokenSendSuccessfulBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_token_send_successful,
                null,
                false);

        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());

        binding.from.setText(mViewModel.getFromAccount());
        binding.to.setText(mViewModel.getToAccount());
        binding.positive.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }

    private void showSendFailureDialog(Throwable error) {
        DialogTokenSendFailureBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_token_send_failure,
                null,
                false);

        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());

        binding.message.setText(ExceptionUtils.getMessage(error));
        binding.positive.setOnClickListener(v -> dialog.dismiss());
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}
