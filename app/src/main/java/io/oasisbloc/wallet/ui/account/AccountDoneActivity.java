package io.oasisbloc.wallet.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.ActivityAccountDoneBinding;
import io.oasisbloc.wallet.ui.splash.SplashActivity;

public class AccountDoneActivity extends BaseActivity {

    private static final String KEY_ACCOUNT = "ACCOUNT";

    public static void start(Context context, String account) {
        Intent starter = new Intent(context, AccountDoneActivity.class);
        starter.putExtra(KEY_ACCOUNT, account);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAccountDoneBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_done);
        binding.accountText.setText(getIntent().getStringExtra(KEY_ACCOUNT));
        binding.action.setOnClickListener(v -> SplashActivity.start(this));
    }

    @Override
    public void onBackPressed() {
    }
}
