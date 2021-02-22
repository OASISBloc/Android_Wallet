package io.oasisbloc.wallet.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.ActivityAccountBinding;

public class AccountActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAccountBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        binding.actionSignIn.setOnClickListener(v -> AccountSignInActivity.start(this));
        binding.actionImport.setOnClickListener(v -> AccountImportActivity.start(this));
        binding.actionCreate.setOnClickListener(v -> AccountCreateActivity.start(this));
    }
}