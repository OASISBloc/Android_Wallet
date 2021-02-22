package io.oasisbloc.wallet.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.databinding.FragmentMainTransactionsBinding;
import io.oasisbloc.wallet.ui.history.TransactionListFragment;

public class MainTransactionsFragment extends BaseFragment {

    private FragmentMainTransactionsBinding mBinding;
    private Fragment mChildFragment = TransactionListFragment.newInstance();

    public static MainTransactionsFragment newInstance() {
        Bundle args = new Bundle();
        MainTransactionsFragment fragment = new MainTransactionsFragment();
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
                R.layout.fragment_main_transactions,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getChildFragmentManager().beginTransaction()
                .replace(mBinding.contents.getId(), mChildFragment)
                .commitNowAllowingStateLoss();
    }
}
