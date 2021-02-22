package io.oasisbloc.wallet.ui.modal;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.PolicyUtils;
import io.oasisbloc.wallet.base.ViewUtils;
import io.oasisbloc.wallet.databinding.ViewAdvanceInputBinding;

@SuppressWarnings("unused")
public class AdvanceInputView extends FrameLayout {

    public enum Type {
        PASSWORD, ACCOUNT, PRIVATE_KRY, EMAIL, RE_INPUT
    }

    private ViewAdvanceInputBinding mBinding;
    private Type mType;
    private AdvanceInputView mSource;
    private int mErrorResId;

    public AdvanceInputView(@NonNull Context context) {
        this(context, null);
    }

    public AdvanceInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvanceInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.view_advance_input,
                this,
                false);
        addView(mBinding.getRoot());

        mBinding.text.addTextChangedListener(createTextWatcher());
        mBinding.error.setVisibility(View.GONE);

        mBinding.clear.setOnClickListener(v -> mBinding.text.setText(""));
        ViewUtils.setToggleAsteriskListener(mBinding.visibility, mBinding.text);
    }

    private TextWatcher createTextWatcher() {
        return ViewUtils.createSimpleTextWatcher(data -> checkValidate());
    }

    private boolean isInputValidation() {
        switch (mType) {
            case ACCOUNT:
                return PolicyUtils.isAccountValidate(getText());
            case PASSWORD:
                return PolicyUtils.isPasswordValidate(getText());
            case EMAIL:
                return PolicyUtils.isEmailValidate(getText());
            case PRIVATE_KRY:
                return PolicyUtils.isPrivateKeyValidate(getText());
            case RE_INPUT:
                return getText().equals(mSource.getText());
            default:
                return true;
        }
    }


    /*
        type
     */

    public void setType(Type type) {
        mType = type;
        switch (mType) {
            case ACCOUNT:
                int lengthAccount = PolicyUtils.getAccountFixMaxLength();
                String digitsAccount = PolicyUtils.getAccountDigits();
                mBinding.text.setKeyListener(DigitsKeyListener.getInstance(digitsAccount));
                mBinding.text.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);
                break;

            case EMAIL:
                mBinding.text.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

            case PASSWORD:
                int lengthPassword = PolicyUtils.getPasswordMaxLength();
                mBinding.text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ViewUtils.setAsterisk(mBinding.text);
                mBinding.text.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(lengthPassword)
                });
                break;

            case PRIVATE_KRY:
                int lengthPrivateKey = PolicyUtils.getPrivateKeyFixLength();
                String digitsPrivateKey = PolicyUtils.getPrivateKeyDigits();
                ViewUtils.setAsterisk(mBinding.text);
                mBinding.text.setKeyListener(DigitsKeyListener.getInstance(digitsPrivateKey));
                mBinding.text.setRawInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            case RE_INPUT:
                setType(mSource.mType);
                mType = Type.RE_INPUT;
                break;

            default:
                break;

        }
        checkValidate();
    }

    public void setSource(AdvanceInputView source) {
        mSource = source;
    }

    private void checkValidate() {
        if (mBinding.text.length() == 0 || isInputValidation()) {
            if (mBinding.error.getVisibility() != View.GONE) {
                mBinding.error.setVisibility(View.GONE);
                mBinding.error.startAnimation(
                        AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
            }
        } else {
            if (mErrorResId != 0) {
                mBinding.error.setText(mErrorResId);
            } else {
                mBinding.error.setText("");
            }
            if (mBinding.error.length() != 0 && mBinding.error.getVisibility() != View.VISIBLE) {
                mBinding.error.setVisibility(View.VISIBLE);
                mBinding.error.startAnimation(
                        AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
            }
        }
    }


    /*
        label
     */

    public void setLabel(@StringRes int resId) {
        mBinding.label.setText(resId);
    }


    /*
        text
     */

    public void setText(String text) {
        mBinding.text.setText(text);
    }

    public void setTextHint(@StringRes int resId) {
        mBinding.text.setHint(resId);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        mBinding.text.removeTextChangedListener(watcher);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mBinding.text.addTextChangedListener(watcher);
    }

    public String getText() {
        return ViewUtils.getText(mBinding.text);
    }


    /*
        action
     */

    public void setClearEnabled(boolean enabled) {
        mBinding.clear.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public void setVisibilityEnabled(boolean enabled) {
        mBinding.visibility.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }


    /*
        error
     */

    public void setError(@StringRes int resId) {
        mErrorResId = resId;
        mBinding.error.setText(resId);
    }

    public void setErrorForce(String msg) {
        mBinding.error.setText(msg);
        if (mBinding.error.getVisibility() != View.VISIBLE) {
            mBinding.error.setVisibility(View.VISIBLE);
            mBinding.error.startAnimation(
                    AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }
    }

    /*
        keyboard
     */
    public void requestFocusAndShowKeyboard() {
        ViewUtils.showKeyboard(mBinding.text);
    }
}
