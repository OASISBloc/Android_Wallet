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
import io.oasisbloc.wallet.databinding.FragmentResourceRamBinding;
import io.oasisbloc.wallet.ui.resource.viewmodel.ResourcesViewModel;

public class ResourcesRamFragment extends BaseFragment {

    private FragmentResourceRamBinding mBinding;

    public static ResourcesRamFragment newInstance() {
        Bundle args = new Bundle();
        ResourcesRamFragment fragment = new ResourcesRamFragment();
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
                R.layout.fragment_resource_ram,
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
        double stake = resources.getRamStake();
        mBinding.balance.setText(DecimalUtils.to(balance, symbol));
        mBinding.stake.setText(DecimalUtils.to(stake, symbol));

        double ramPercent = resources.getRamAvailable() / resources.getRamTotal();
        mBinding.ramGraph.setMax(Integer.MAX_VALUE);
        mBinding.ramGraph.setProgress((int) (Integer.MAX_VALUE * ramPercent));
        mBinding.ramPercent.setText(DecimalUtils.toPercent(ramPercent));
        mBinding.ramInfo.setText(getString(
                R.string.resource_format_info,
                DecimalUtils.toVolume(resources.getRamAvailable()),
                DecimalUtils.toVolume(resources.getRamTotal())));
    }
}
