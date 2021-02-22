package io.oasisbloc.wallet.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.ActivityAppLockBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.setting.viewmodel.AppLockViewModel;

public class AppLockActivity extends BaseActivity {

    private static final String KEY_MODE_SETTING = "MODE_SETTING";

    private ActivityAppLockBinding mBinding;
    private AppLockViewModel mViewModel;

    public static void startForChecking(Context context) {
        Intent starter = new Intent(context, AppLockActivity.class);
        starter.putExtra(KEY_MODE_SETTING, false);
        context.startActivity(starter);
    }

    public static void startForSetting(Context context) {
        Intent starter = new Intent(context, AppLockActivity.class);
        starter.putExtra(KEY_MODE_SETTING, true);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableAppLockPace();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_lock);
        mViewModel = AppLockViewModel.get(this);

        mBinding.cancel.setOnClickListener(v -> mViewModel.onBackPressed());
        mBinding.keypad.key1.setOnClickListener(v -> mViewModel.appendCode(1));
        mBinding.keypad.key2.setOnClickListener(v -> mViewModel.appendCode(2));
        mBinding.keypad.key3.setOnClickListener(v -> mViewModel.appendCode(3));
        mBinding.keypad.key4.setOnClickListener(v -> mViewModel.appendCode(4));
        mBinding.keypad.key5.setOnClickListener(v -> mViewModel.appendCode(5));
        mBinding.keypad.key6.setOnClickListener(v -> mViewModel.appendCode(6));
        mBinding.keypad.key7.setOnClickListener(v -> mViewModel.appendCode(7));
        mBinding.keypad.key8.setOnClickListener(v -> mViewModel.appendCode(8));
        mBinding.keypad.key9.setOnClickListener(v -> mViewModel.appendCode(9));
        mBinding.keypad.key0.setOnClickListener(v -> mViewModel.appendCode(0));
        mBinding.keypad.keyBack.setOnClickListener(v -> mViewModel.removeCode());

        mViewModel.getStepLiveData().observe(this, this::observeStep);
        mViewModel.getRouteLiveData().observe(this, this::observeRoute);
        mViewModel.getToastLiveData().observe(this, this::showToast);
        mViewModel.getLogoLiveData().observe(this, mBinding.logo::setImageResource);
        mViewModel.getTitleLiveData().observe(this, mBinding.title::setText);
        mViewModel.getMessageLiveData().observe(this, mBinding.message::setText);
        mViewModel.getErrorLiveData().observe(this, mBinding.error::setText);
        mViewModel.getCodeCountLiveData().observe(this, this::observeCodeCount);
        mViewModel.getBlockTimerLiveData().observe(this, mBinding.timer::setText);

        if (getIntent().getBooleanExtra(KEY_MODE_SETTING, false)) {
            mBinding.cancel.setVisibility(View.VISIBLE);
            mViewModel.setSettingMode();
        } else {
            mViewModel.setCheckingMode();
            mBinding.cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {
        mViewModel.onBackPressed();
    }

    private void observeStep(AppLockViewModel.Step step) {
        if (step == AppLockViewModel.Step.BLOCK) {
            mBinding.keypad.getRoot().setVisibility(View.GONE);
            mBinding.timer.setVisibility(View.VISIBLE);
        } else {
            mBinding.keypad.getRoot().setVisibility(View.VISIBLE);
            mBinding.timer.setVisibility(View.GONE);
        }
    }

    private void observeCodeCount(Integer count) {
        mBinding.keypad.dot1.setSelected(count >= 1);
        mBinding.keypad.dot2.setSelected(count >= 2);
        mBinding.keypad.dot3.setSelected(count >= 3);
        mBinding.keypad.dot4.setSelected(count >= 4);
    }

    private void observeRoute(AppLockViewModel.Route route) {
        switch (route) {
            case CANCEL_DIALOG:
                new CenterModal(this)
                        .setCancelable(false)
                        .setMessage(R.string.applock_msg_continue)
                        .setNegativeButton(R.string.action_quit, (d, w) -> mViewModel.cancel())
                        .setPositiveButton(R.string.action_continue, null)
                        .show();
                break;

            case BACK_ACTIVITY:
                finish();
                break;

            case PHONE_HOME:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                break;
        }
    }
}
