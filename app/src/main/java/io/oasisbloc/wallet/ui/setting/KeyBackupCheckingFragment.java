package io.oasisbloc.wallet.ui.setting;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseFragment;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.databinding.FragmentKeyBackupCheckingBinding;
import io.oasisbloc.wallet.ui.modal.AdvanceInputView;
import io.oasisbloc.wallet.ui.setting.viewmodel.KeyBackupViewModel;

public class KeyBackupCheckingFragment extends BaseFragment {

    private FragmentKeyBackupCheckingBinding mBinding;
    private KeyBackupViewModel mViewModel;

    static KeyBackupCheckingFragment newInstance() {
        Bundle args = new Bundle();
        KeyBackupCheckingFragment fragment = new KeyBackupCheckingFragment();
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
                R.layout.fragment_key_backup_checking,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.password.setType(AdvanceInputView.Type.PASSWORD);
        mBinding.password.setLabel(R.string.account_label_password);
        mBinding.password.addTextChangedListener(createInputWatcher());
        mBinding.password.setTextHint(R.string.settings_key_backup_password_hint);
        mBinding.password.requestFocusAndShowKeyboard();

        mBinding.action.setOnClickListener(v -> onActionClicked());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = KeyBackupViewModel.get(requireActivity());
        mViewModel.getInputValidateLiveDate().observe(this, mBinding.action::setEnabled);
        mViewModel.getPasswordInputError().observe(this, mBinding.password::setErrorForce);
    }

    private TextWatcher createInputWatcher() {
        return ViewUtils.createSimpleTextWatcher(data ->
                mViewModel.setPassword(mBinding.password.getText())
        );
    }

    private void onActionClicked() {
        ViewUtils.hideKeyboard(getBaseActivity());
        mViewModel.checkPassword();
    }
}
