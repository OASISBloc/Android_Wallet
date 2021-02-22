package io.oasisbloc.wallet.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.ActivitySplashBinding;
import io.oasisbloc.wallet.ui.account.AccountActivity;
import io.oasisbloc.wallet.ui.setting.AppLockActivity;
import io.oasisbloc.wallet.ui.main.MainActivity;
import io.oasisbloc.wallet.ui.splash.viewmodel.SplashViewModel;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class SplashActivity extends BaseActivity {

    public static Intent createStater(Context context) {
        Intent starter = new Intent(context, SplashActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        return starter;
    }

    public static void start(Context context) {
        context.startActivity(createStater(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        SplashViewModel viewModel = SplashViewModel.get(this);
        viewModel.getRouteLiveData().observe(this, this::handleRoute);
        viewModel.getMessageLiveData().observe(this, binding.message::setText);

        WalletListViewModel walletModel = WalletListViewModel.get(this, getLifecycle());
        String myList = walletModel.getMyTokenList();

        if(myList.length() == 0){
            JsonArray arr = new JsonArray();
            arr.add("OSB");

            walletModel.setMyTokenList(arr.toString());
        }
    }

    private void handleRoute(SplashRoute route) {
        switch (route) {
            case PIN_CHECKING:
                MainActivity.start(this);
                AppLockActivity.startForChecking(this);
                finish();
                break;
            case PIN_SETTING:
                MainActivity.start(this);
                AppLockActivity.startForSetting(this);
                finish();
                break;

            case MAIN:
                MainActivity.start(this);
                finish();
                break;

            case ACCOUNT:
                AccountActivity.start(this);
                finish();
                break;
        }
    }
}