package io.oasisbloc.wallet.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.ActivityMainBinding;
import io.oasisbloc.wallet.ui.setting.SettingsFragment;
import io.oasisbloc.wallet.ui.wallet.WalletListFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;

    private Fragment mWalletFragment = WalletListFragment.newInstance();
    private Fragment mTransactionFragment = MainTransactionsFragment.newInstance();
    private Fragment mMyDataFragment = MainMyDataFragment.newInstance();
    private Fragment mMarketplaceFragment = MainMarketplaceFragment.newInstance();
    private Fragment mSettingsFragment = SettingsFragment.newInstance();

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.navigation.setOnNavigationItemSelectedListener(this::onNavigationClicked);

        setFirstContents();
    }

    private boolean onNavigationClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wallet:
                setContents(mWalletFragment);
                return true;
            case R.id.transaction:
                setContents(mTransactionFragment);
                return true;
            case R.id.data:
                setContents(mMyDataFragment);
                return true;
            case R.id.marketplace:
                setContents(mMarketplaceFragment);
                return true;
            case R.id.settings:
                setContents(mSettingsFragment);
                return true;
        }

        return false;
    }

    private void setFirstContents() {
        setContents(new WalletListFragment());
    }

    private void setContents(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.container.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitNowAllowingStateLoss();
    }
}
