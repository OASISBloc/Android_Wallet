package io.oasisbloc.wallet.ui.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseActivity;
import io.oasisbloc.wallet.databinding.DialogKeyBackupAlertBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.setting.viewmodel.KeyBackupViewModel;

public class KeyBackupActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, KeyBackupActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBarBackButtonEnable();
        setContentView(R.layout.activity_key_backup);

        KeyBackupViewModel viewModel = KeyBackupViewModel.get(this);
        viewModel.getPasswordCheckingLiveData().observe(
                this,
                this::observePasswordCheckingSuccess,
                null,
                this::handleProgressDialog);

        replaceFragment(KeyBackupCheckingFragment.newInstance());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
    }

    private void observePasswordCheckingSuccess() {
        showAlertDialog();
        replaceFragment(KeyBackupShareFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    private void showAlertDialog() {
        DialogKeyBackupAlertBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_key_backup_alert,
                null,
                false
        );
        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());
        binding.action.setOnClickListener(v -> dialog.dismiss());
    }

    public void onClickLeftActionBarBtn(View view){
        if(view.getId() == R.id.actionbar_left){
            super.onBackPressed();
        }
    }
}
