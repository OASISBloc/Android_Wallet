package io.oasisbloc.wallet.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.databinding.FragmentKeyBackupShareBinding;
import io.oasisbloc.wallet.ui.setting.viewmodel.KeyBackupViewModel;

public class KeyBackupShareFragment extends BaseFragment {

    private FragmentKeyBackupShareBinding mBinding;
    private KeyBackupViewModel mViewModel;

    static KeyBackupShareFragment newInstance() {
        Bundle args = new Bundle();
        KeyBackupShareFragment fragment = new KeyBackupShareFragment();
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
                R.layout.fragment_key_backup_share,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.setAsterisk(mBinding.privateKeyText);
        ViewUtils.setToggleAsteriskListener(mBinding.privateKeyHide, mBinding.privateKeyText);
        mBinding.action.setOnClickListener(v -> onBackupClicked());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = KeyBackupViewModel.get(requireActivity());
        mViewModel.getPublicKeyLiveDate().observe(this, mBinding.publicKeyText::setText);
        mViewModel.getPrivateKeyLiveDate().observe(this, mBinding.privateKeyText::setText);
    }

    private void onBackupClicked() {
        getBaseActivity().launchShare(mViewModel.getBackupText());
    }
}
