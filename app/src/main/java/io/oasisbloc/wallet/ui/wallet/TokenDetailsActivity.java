package io.oasisbloc.wallet.ui.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.DecimalUtils;
import io.oasisbloc.wallet.data.LockupHistory;
import io.oasisbloc.wallet.data.Token;
import io.oasisbloc.wallet.data.TransactionHistory;
import io.oasisbloc.wallet.databinding.ActivityTokenDetailsBinding;
import io.oasisbloc.wallet.databinding.DialogAccountQrcodeBinding;
import io.oasisbloc.wallet.databinding.DialogSendSelectBinding;
import io.oasisbloc.wallet.ui.account.QRCodeUtil;
import io.oasisbloc.wallet.ui.account.ScanQrActivity;
import io.oasisbloc.wallet.ui.history.LockupHistoryFragment;
import io.oasisbloc.wallet.ui.history.TransactionListFragment;
import io.oasisbloc.wallet.ui.history.viewmodel.LockupHistoryViewModel;
import io.oasisbloc.wallet.ui.history.viewmodel.TransactionHistoryViewModel;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class TokenDetailsActivity extends BaseActivity {

    private static final String KEY_SYMBOL = "SYMBOL";

    private ActivityTokenDetailsBinding mBinding;

    private Dialog dialog;

    private WalletListViewModel mViewModel;
    private String accountId;
    private String symbol;
    private TransactionHistoryViewModel transactionVM;

    public static void start(Context context, String symbol) {
        Intent starter = new Intent(context, TokenDetailsActivity.class);
        starter.putExtra(KEY_SYMBOL, symbol);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        symbol = getIntent().getStringExtra(KEY_SYMBOL);


        //symbol 로 logo Select
        int logo;
        if(symbol.equals("OSB") == true) {
            logo = R.drawable._temp_obs_ic_1;
        }else if(symbol.equals("SOSB") == true){
            logo = R.drawable.temp_sosb_ic_1;
        }else if(symbol.equals("CRA") == true){
            logo = R.drawable.temp_cra_ic_1;
        }else{
            logo = R.drawable._temp_obs_ic_1;
        }

        mViewModel = WalletListViewModel.get(this, getLifecycle());
        mViewModel.getAccount().observe(this, this::setAccountId);



        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_token_details);
        mBinding.title.setText(symbol);
        mBinding.logo.setImageResource(logo);
        mBinding.send.setOnClickListener(v -> showAlertDialog());
//        mBinding.send.setOnClickListener(v -> TokenSendActivity.start(this, symbol));

        mBinding.receive.setOnClickListener(v -> onShowQRCode(accountId));
        mBinding.viewPager.setAdapter(new MyViewPagerAdapter());
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        transactionVM = TransactionHistoryViewModel.get(this);

        setSupportActionBar(mBinding.toolbarView);
        actionBarBackButtonEnable();
        setTransactionHistory(symbol, null);
        setLockupHistory(symbol, null);

        transactionVM.setSymbol(symbol);
        transactionVM.getHistoryResult().observe(
                this,
                result -> setTransactionHistory(symbol, result),
                error -> setTransactionHistory(symbol, null),
                null);

        LockupHistoryViewModel lockupVM = LockupHistoryViewModel.get(this);
        lockupVM.getHistoryResult().observe(
                this,
                result -> setLockupHistory(symbol, result),
                error -> setLockupHistory(symbol, null),
                null);

        mViewModel.getBalances().observe(this, this::observeTokens);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }

    private void observeTokens(List<Token> list) {
        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); i++){
                Token token = list.get(i);

                if(token.getSymbol().equals(symbol) == true){
                    mBinding.balance.setText(DecimalUtils.to(token.getBalance(), symbol));
                }
            }
        }
    }

    private void setTransactionHistory(String symbol, TransactionHistory history) {
//        if (history == null) {
//            mBinding.balance.setText(DecimalUtils.to(0, symbol));
//        } else {
//            mBinding.balance.setText(DecimalUtils.to(history.getTotalBalance(), symbol));
//        }
//        transactionVM.setSymbol("");
    }

    private void setLockupHistory(String symbol, LockupHistory history) {
        if (history == null || history.isEmpty()) {
            mBinding.lockupLayout.setVisibility(View.GONE);
            mBinding.viewPager.disableScroll(true);
            TabLayout.Tab lockupTab = mBinding.tabLayout.getTabAt(1);
            if (lockupTab != null) {
                ((ViewGroup) lockupTab.view).setVisibility(View.INVISIBLE);
            }
        } else {
            mBinding.lockupLayout.setVisibility(View.VISIBLE);
//            mBinding.lockup.setText(DecimalUtils.to(history.getTotalBalance(), symbol));
            mBinding.viewPager.disableScroll(false);
            TabLayout.Tab lockupTab = mBinding.tabLayout.getTabAt(1);
            if (lockupTab != null) {
                ((ViewGroup) lockupTab.view).setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private BaseFragment[] mFragments = new BaseFragment[]{
                TransactionListFragment.newInstance(),
                LockupHistoryFragment.newInstance()
        };

        private MyViewPagerAdapter() {
            super(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return getString(R.string.wallet_tab_transactions);
            else if (position == 1) return getString(R.string.wallet_tab_lockup);
            else return "";
        }
    }

    public void gotoSendTokenFromQR(){
        dialog.dismiss();
        String symbol = getIntent().getStringExtra(KEY_SYMBOL);
        TokenSendActivity.start(this, symbol, "");
    }

    private void gotoQRReader(){
        dialog.dismiss();
        String symbol = getIntent().getStringExtra(KEY_SYMBOL);
        ScanQrActivity.start(this, symbol);
    }

    private void gotoSendToken(){
        dialog.dismiss();
        TokenSendActivity.start(this, symbol, "");
    }

    private void showAlertDialog() {
        DialogSendSelectBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_send_select,
                null,
                false
        );
        dialog = CenterModal.showCenterModal(binding.getRoot());
        binding.qrcode.setOnClickListener(v -> gotoQRReader());
        binding.directInput.setOnClickListener(v -> gotoSendToken());
        binding.cancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void setAccountId(String id){
        accountId = id;
    }

    private void onShowQRCode(String accountId){
        //yskim qrcode generate
        Bitmap bitmap = QRCodeUtil.encodeAsBitmap(accountId, 300, 300);

        //image set
        DialogAccountQrcodeBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_account_qrcode,
                null,
                false
        );
        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());
        binding.qrImage.setImageBitmap(bitmap);
        binding.positive.setOnClickListener(v -> dialog.dismiss());
    }
}
