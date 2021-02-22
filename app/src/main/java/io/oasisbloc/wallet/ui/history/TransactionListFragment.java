package io.oasisbloc.wallet.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.ExceptionUtils;
import io.oasisbloc.wallet.data.Transaction;
import io.oasisbloc.wallet.data.TransactionHistory;
import io.oasisbloc.wallet.databinding.FragmentHistoryListBinding;
import io.oasisbloc.wallet.ui.history.adapter.TransactionHistoryAdapter;
import io.oasisbloc.wallet.ui.history.viewmodel.TransactionHistoryViewModel;

public class TransactionListFragment extends BaseFragment {

    private TransactionHistoryAdapter mAdapter = new TransactionHistoryAdapter();
    private TransactionHistoryViewModel mViewModel;
    private FragmentHistoryListBinding mBinding;
    private static String mToken;

    public static TransactionListFragment newInstance() {
        Bundle args = new Bundle();
        TransactionListFragment fragment = new TransactionListFragment();
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
                R.layout.fragment_history_list,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.messageView.setVisibility(View.GONE);
        mBinding.refreshLayout.setOnRefreshListener(this::onRefresh);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setCallback(this::onItemClicked);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                RecyclerView.LayoutManager lm = mBinding.recyclerView.getLayoutManager();
                if (positionStart == 0 && lm instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) lm).scrollToPositionWithOffset(0, 0);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = TransactionHistoryViewModel.get(requireActivity());
        mViewModel.getHistoryResult().observe(this,
                this::observeSuccess,
                this::observeError,
                this::observeLoading);
    }

    private void onRefresh() {
        mBinding.messageView.setVisibility(View.GONE);
        mViewModel.refresh();
    }

    private void onItemClicked(Transaction item) {
        HistoryDetailsActivity.start(requireContext(), item);
    }

    private void observeSuccess(TransactionHistory history) {
        mBinding.progressView.setVisibility(View.GONE);
        mBinding.messageView.setVisibility(history.isEmpty() ? View.VISIBLE : View.GONE);
        mBinding.messageView.setText(getString(R.string.history_details_msg_empty_transaction));
        mAdapter.submitList(history);
    }

    private void observeError(Throwable error) {
        mBinding.progressView.setVisibility(View.GONE);
        mBinding.messageView.setVisibility(View.VISIBLE);
        mBinding.messageView.setText(ExceptionUtils.getMessage(error));
        mAdapter.submitList(new ArrayList<>());
    }

    private void observeLoading(Boolean loading) {
        if (loading) {
            mBinding.progressView.setVisibility(View.VISIBLE);
            mBinding.messageView.setVisibility(View.GONE);
        } else {
            mBinding.progressView.setVisibility(View.GONE);
            mBinding.refreshLayout.setRefreshing(false);
        }
    }

    public void setToken(String token){
        mToken = token;
    }
}