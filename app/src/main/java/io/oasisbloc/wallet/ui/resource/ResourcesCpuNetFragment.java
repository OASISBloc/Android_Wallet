package io.oasisbloc.wallet.ui.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.DecimalUtils;
import io.oasisbloc.wallet.data.TokenResources;
import io.oasisbloc.wallet.databinding.FragmentResourceCpuNetBinding;
import io.oasisbloc.wallet.ui.resource.viewmodel.ResourcesViewModel;

public class ResourcesCpuNetFragment extends BaseFragment {

    private FragmentResourceCpuNetBinding mBinding;

    public static ResourcesCpuNetFragment newInstance() {
        Bundle args = new Bundle();
        ResourcesCpuNetFragment fragment = new ResourcesCpuNetFragment();
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
                R.layout.fragment_resource_cpu_net,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResourcesViewModel viewModel;
        viewModel = ResourcesViewModel.get(requireActivity(), requireActivity().getLifecycle());
        viewModel.getResources().observe(this, this::handleSuccess, null, null);
    }

    private void handleSuccess(TokenResources resources) {
        String symbol = resources.getSymbol();

        double balance = resources.getBalance();
        double stake = resources.getCpuStake() + resources.getNetStake();
        mBinding.balance.setText(DecimalUtils.to(balance, symbol));
        mBinding.stake.setText(DecimalUtils.to(stake, symbol));

        double cpuPercent = resources.getCpuAvailable() / resources.getCpuTotal();
        mBinding.cpuGraph.setMax(Integer.MAX_VALUE);
        mBinding.cpuGraph.setProgress((int) (Integer.MAX_VALUE * cpuPercent));
        mBinding.cpuPercent.setText(DecimalUtils.toPercent(cpuPercent));
        mBinding.cpuStake.setText(DecimalUtils.to(resources.getCpuStake(), symbol));
        mBinding.cpuInfo.setText(getString(
                R.string.resource_format_info,
                DecimalUtils.toSpeed(resources.getCpuAvailable()),
                DecimalUtils.toSpeed(resources.getCpuTotal())));
        mBinding.cpuRefunding.setText(getString(
                R.string.resource_format_refunding_cpu,
                DecimalUtils.to(resources.getCpuRefunding(), symbol)));

        double netPercent = resources.getNetAvailable() / resources.getNetTotal();
        mBinding.netGraph.setMax(Integer.MAX_VALUE);
        mBinding.netGraph.setProgress((int) (Integer.MAX_VALUE * netPercent));
        mBinding.netPercent.setText(DecimalUtils.toPercent(netPercent));
        mBinding.netStake.setText(DecimalUtils.to(resources.getNetStake(), symbol));
        mBinding.netInfo.setText(getString(
                R.string.resource_format_info,
                DecimalUtils.toVolume(resources.getNetAvailable()),
                DecimalUtils.toVolume(resources.getNetTotal())));
        mBinding.netRefunding.setText(getString(
                R.string.resource_format_refunding_net,
                DecimalUtils.to(resources.getNetRefunding(), symbol)));
    }
}
