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
import io.oasisbloc.wallet.data.Result;
import io.oasisbloc.wallet.databinding.FragmentSettingsBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.splash.SplashActivity;
import io.oasisbloc.wallet.viewmodel.SettingsViewModel;

public class SettingsFragment extends BaseFragment {

    private FragmentSettingsBinding mBinding;
    private SettingsViewModel mViewModel;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
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
                R.layout.fragment_settings,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.accountCopy.setOnClickListener(v -> onAccountCopyClicked());
        mBinding.keyBackup.setOnClickListener(v -> onKeyBackupClicked());
        mBinding.resetPassword.setOnClickListener(v -> onResetPasswordClicked());
        mBinding.appLockPassword.setOnClickListener(v -> onAppLockClicked());
        mBinding.helpCenter.setOnClickListener(v -> onHelpCenterClicked());
        mBinding.appVersion.setOnClickListener(v -> onVersionClicked());
        mBinding.logout.setOnClickListener(v -> onLogoutClicked());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = SettingsViewModel.get(requireActivity(), this);

        mViewModel.getAccount().observe(this, mBinding.accountText::setText);
        mViewModel.getAppLockEnabled().observe(this, mBinding.appLockPasswordSwitch::setChecked);
        mViewModel.getVersion().observe(this, mBinding.versionText::setText);
        mViewModel.getLogoutResult().observe(this, this::observeLogoutResult);
    }

    private void onAccountCopyClicked() {
        ViewUtils.copyToClipboard(mBinding.accountText);
        getBaseActivity().showToast(R.string.msg_copied);
    }

    private void onKeyBackupClicked() {
        KeyBackupActivity.start(requireContext());
    }

    private void onResetPasswordClicked() {
        ResetPasswordActivity.start(requireContext(),getString(R.string.settings_reset_password_subtitle));
    }

    private void onAppLockClicked() {
        AppLockActivity.startForSetting(requireContext());
    }

    private void onHelpCenterClicked() {
        getBaseActivity().launchEmailApp(
                mViewModel.getHelpCenterEmail(),
                mViewModel.getHelpCenterSubject(),
                mViewModel.getHelpCenterBody());
    }

    private void onVersionClicked() {
        getBaseActivity().launchUpdater();
    }

    private void onLogoutClicked() {
        new CenterModal(requireContext())
                .setTitle(R.string.settings_logout_alert_title)
                .setPositiveButton(R.string.action_no, null)
                .setNegativeButton(R.string.settings_logout_action, (dialog, which) -> {
                    getBaseActivity().showProgressDialog();
                    mViewModel.logout();
                })
                .show();
    }

    private void observeLogoutResult(Result result) {
        getBaseActivity().dismissProgressDialog();
        if (result.isSuccessful()) {
            SplashActivity.start(requireContext());
        }
        if (result.isFailure()) {
            getBaseActivity().showToast(result.getError());
        }
    }
}
