package io.oasisbloc.wallet.ui.addtoken;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.databinding.ActivitySearchTokenBinding;
import io.oasisbloc.wallet.ui.addtoken.adapter.AddTokenListAdater;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class AddTokenActivity extends BaseActivity {

    private WalletListViewModel mViewModel;
    private ActivitySearchTokenBinding mBinding;
    private List<String> showList = new ArrayList<>();
    private List<String> searchList = new ArrayList<>();;
    private AddTokenListAdater adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddTokenActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_token);
        mViewModel = WalletListViewModel.get(this, getLifecycle());

        showList = mViewModel.getTokenList();
        searchList = mViewModel.getTokenList();

        adapter = new AddTokenListAdater(this, showList, mViewModel);
        mBinding.listview.setAdapter(adapter);

        //Error
        mBinding.progressView.setVisibility(View.GONE);
        if(showList.size() == 0) {
            mBinding.messageView.setText("No Search Tokens");
        }

        mBinding.searchView.searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = "" + s;
                search(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void search(String searchStr){
        boolean isFind = false;
        showList.clear();

        if(searchStr.length() == 0){
            isFind = true;
            adapter = null;
            showList = mViewModel.getTokenList();
            adapter = new AddTokenListAdater(this, showList, mViewModel);
            mBinding.listview.setAdapter(adapter);
        }else{
            for(int i = 0; i < searchList.size(); i++){
                String txt = searchStr.toUpperCase();
                if(searchList.get(i).contains(txt) == true){
                    isFind = true;
                    showList.add(searchList.get(i));
                }
            }
        }
        if(!isFind) {
            mBinding.messageView.setText("No Search Tokens");
        }else{
            mBinding.messageView.setText("");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}