package io.oasisbloc.wallet.ui.wallet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.DecimalUtils;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.data.Token;
import io.oasisbloc.wallet.databinding.DialogAccountQrcodeBinding;
import io.oasisbloc.wallet.databinding.DialogGeneralOneButtonNoTitleBinding;
import io.oasisbloc.wallet.databinding.DialogKeyBackupAlertBinding;
import io.oasisbloc.wallet.databinding.FragmentWalletListBinding;
import io.oasisbloc.wallet.ui.account.QRCodeUtil;
import io.oasisbloc.wallet.ui.addtoken.AddTokenActivity;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.resource.ResourcesActivity;
import io.oasisbloc.wallet.ui.wallet.adapter.TokenListAdapter;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class WalletListFragment extends BaseFragment {

    private FragmentWalletListBinding mBinding;
    private WalletListViewModel mViewModel;

    private List<Token> balanceList;
    private List<String> myTokenList = new ArrayList<>();
    private TokenListAdapter adapter;

    public static WalletListFragment newInstance() {
        Bundle args = new Bundle();
        WalletListFragment fragment = new WalletListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_wallet_list,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.summary.copy.setOnClickListener(v -> onCopyClicked());
        mBinding.limit.root.setOnClickListener(v -> onResourceClicked());
        mBinding.tokenAdd.setOnClickListener(v -> onTokenAddClicked());
//        mBinding.tokenAdd.setVisibility(View.GONE);
//        mBinding.token.root.setOnClickListener(v -> onTokenItemClicked());

        //yskim qrcode generate
//        mBinding.summary.account.setOnClickListener(v -> onShowQRCode(mBinding.summary.account.getText().toString()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = WalletListViewModel.get(requireActivity(), getLifecycle());

        mViewModel.getAccount().observe(this, mBinding.summary.account::setText);
//        mViewModel.getBalance().observe(this, mBinding.summary.balance::setText);
        mViewModel.getBalances().observe(this, this::setList);
//        mViewModel.getSymbol().observe(this, mBinding.summary.symbol::setText);

        mViewModel.getCpuPercent().observe(this, this::observeCpuPercent);
        mViewModel.getNetPercent().observe(this, this::observeNetPercent);
        mViewModel.getRamPercent().observe(this, this::observeRamPercent);

        mViewModel.getSymbols().observe(this, this::observeTokenList);
        mViewModel.getBalances().observe(this, this::observeTokens);
    }

    private void setList(List<Token> list){
        this.balanceList = list;

        adapter = new TokenListAdapter(getBaseActivity(), this.balanceList, mViewModel);
        adapter.setCallback(this::onItemLongClicked);
        mBinding.listview.setAdapter(adapter);
    }

    public void onItemLongClicked(int position, String symbol){
        if(symbol.equals("OSB") == true){
            showDialog(true, position, "");
        }else{
            showDialog(false, position, symbol);
        }
    }

    private void showDialog(boolean isDefault, int position, String symbol){
        DialogGeneralOneButtonNoTitleBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_general_one_button_no_title,
                null,
                false
        );
        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());

        if(isDefault) {
            binding.message.setText("Cannot approved.");
        }else{
            String msg = symbol + " is deleted.";
            binding.message.setText(msg);
        }
        binding.positive.setText("Confirm");

        binding.positive.setOnClickListener(v -> {

            if(!isDefault){
                String myTokens = mViewModel.getMyTokenList();
                JsonParser parser = new JsonParser();
                JsonArray arr = parser.parse(myTokens).getAsJsonArray();

                for (int i = 0; i < arr.size(); i++) {
                    if (symbol.equals(arr.get(i).getAsString()) == true) {
                        arr.remove(i);
                        break;
                    }
                }
                mViewModel.setMyTokenList(arr.toString());
                balanceList.remove(position);
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        });
    }


    private void observeCpuPercent(double percent) {
        mBinding.limit.cpuPercent.setText(DecimalUtils.toPercent(percent));
        mBinding.limit.cpuGraph.setMax(Integer.MAX_VALUE);
        mBinding.limit.cpuGraph.setProgress((int) (percent * Integer.MAX_VALUE));
    }

    private void observeNetPercent(double percent) {
        mBinding.limit.netPercent.setText(DecimalUtils.toPercent(percent));
        mBinding.limit.netGraph.setMax(Integer.MAX_VALUE);
        mBinding.limit.netGraph.setProgress((int) (percent * Integer.MAX_VALUE));
    }

    private void observeRamPercent(double percent) {
        mBinding.limit.ramPercent.setText(DecimalUtils.toPercent(percent));
        mBinding.limit.ramGraph.setMax(Integer.MAX_VALUE);
        mBinding.limit.ramGraph.setProgress((int) (percent * Integer.MAX_VALUE));
    }

    private void observeTokens(List<Token> list) {
        if (!list.isEmpty()) {
//            mBinding.token.name.setText(list.get(0).getSymbol());
//            mBinding.token.balance.setText(DecimalUtils.to(list.get(0).getBalance()));
        }
    }

    private void observeTokenList(List<String> list) {
        Log.i("test", "List = " + list.toString());
    }

    private void onCopyClicked() {
        ViewUtils.copyToClipboard(mBinding.summary.account);
        getBaseActivity().showToast(R.string.msg_copied);
    }

    private void onResourceClicked() {
        ResourcesActivity.start(requireContext());
    }

    private void onTokenAddClicked() {
        //Token Add Activity 이동
        AddTokenActivity.start(requireContext());
    }

    private void onTokenItemClicked() {
        TokenDetailsActivity.start(requireContext(), "OSB");
    }

//    private void onShowQRCode(String accountId){
//        //yskim qrcode generate
//        Bitmap bitmap = QRCodeUtil.encodeAsBitmap(accountId, 300, 300);
//
//        //image set
//        showAlertDialog(bitmap);
//    }

//    private void showAlertDialog(Bitmap bitmap) {
//        DialogAccountQrcodeBinding binding = DataBindingUtil.inflate(
//                getLayoutInflater(),
//                R.layout.dialog_account_qrcode,
//                null,
//                false
//        );
//        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());
//        binding.qrImage.setImageBitmap(bitmap);
//        binding.positive.setOnClickListener(v -> dialog.dismiss());
//    }
}
