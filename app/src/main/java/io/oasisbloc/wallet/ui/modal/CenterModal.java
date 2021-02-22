package io.oasisbloc.wallet.ui.modal;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.databinding.DialogGeneralBinding;

public class CenterModal extends MaterialAlertDialogBuilder {

    private AlertDialog mDialog;
    private DialogGeneralBinding mBinding;

    public CenterModal(Context context) {
        super(context, R.style.ThemeOverlay_Accent);
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_general,
                null,
                false);
        mBinding.title.setVisibility(View.GONE);
        mBinding.message.setVisibility(View.GONE);
        mBinding.positive.setVisibility(View.GONE);
        mBinding.negative.setVisibility(View.GONE);
        setView(mBinding.getRoot());
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setTitle(@Nullable CharSequence title) {
        mBinding.title.setVisibility(View.VISIBLE);
        mBinding.title.setText(title);
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setMessage(int messageId) {
        setMessage(getContext().getString(messageId));
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setMessage(@Nullable CharSequence message) {
        mBinding.message.setVisibility(View.VISIBLE);
        mBinding.message.setText(message);
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        setPositiveButton(getContext().getString(textId), listener);
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        mBinding.positive.setVisibility(View.VISIBLE);
        mBinding.positive.setText(text);
        mBinding.positive.setOnClickListener(v -> {
            mDialog.dismiss();
            if (listener != null) {
                listener.onClick(mDialog, 0);
            }
        });
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
        setNegativeButton(getContext().getString(textId), listener);
        return this;
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        mBinding.negative.setVisibility(View.VISIBLE);
        mBinding.negative.setText(text);
        mBinding.negative.setOnClickListener(v -> {
            mDialog.dismiss();
            if (listener != null) {
                listener.onClick(mDialog, 0);
            }
        });
        return this;
    }

    @Override
    public AlertDialog show() {
        mDialog = super.show();

        Window window = mDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.75f;
            mDialog.getWindow().setAttributes(lp);
        }

        return mDialog;
    }


    public static AlertDialog showCenterModal(View view) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(view.getContext())
                .setCancelable(false)
                .setView(view)
                .show();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.75f;
            dialog.getWindow().setAttributes(lp);
        }

        return dialog;
    }
}
