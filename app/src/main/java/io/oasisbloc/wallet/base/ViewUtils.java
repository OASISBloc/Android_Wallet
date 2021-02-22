package io.oasisbloc.wallet.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import io.oasisbloc.wallet.base.action.Action1;

public class ViewUtils {

    public static String getText(TextView view) {
        if (view == null) return "";
        CharSequence charSequence = view.getText();
        if (charSequence == null) return "";
        return charSequence.toString();
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (android.content.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public static void copyToClipboard(TextView view) {
        ClipboardManager clipboard = (android.content.ClipboardManager) view.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText("Copied Text", getText(view));
        clipboard.setPrimaryClip(clip);
    }


    /*
        asterisk
     */

    public static void setAsterisk(TextView view) {
        view.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    public static void setToggleAsteriskListener(View button, TextView textview) {
        button.setOnClickListener(v -> {
            toggleAsterisk(textview);
            button.setSelected((textview.getTransformationMethod() instanceof HideReturnsTransformationMethod));
        });
    }

    public static void setToggleAsteriskListener(View button, EditText text) {
        button.setOnClickListener(v -> {
            int selection = text.getSelectionEnd();
            toggleAsterisk(text);
            text.setSelection(selection);
            button.setSelected((text.getTransformationMethod() instanceof HideReturnsTransformationMethod));
        });
    }

    private static void toggleAsterisk(TextView text) {
        if (text.getTransformationMethod() instanceof HideReturnsTransformationMethod) {
            setAsterisk(text);
        } else {
            text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }


    public static void initAmountEditText(EditText view) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    if (s.charAt(0) == '0' && s.charAt(1) != '.') {
                        view.removeTextChangedListener(this);
                        int index = view.getSelectionEnd();
                        String text = ViewUtils.getText(view);
                        view.setText(text.replace("0", "0."));
                        view.setSelection(index + 1);
                        view.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        view.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {
                if (source.equals(".") || source.equals(",")) {
                    return "";
                }
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > 4) {
                    return "";
                }
            }
            return null;
        }});


    }

    public static void addTextChangedListener(TextView view, Action1<String> action) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action.onAction(s == null ? "" : s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static TextWatcher createSimpleTextWatcher(Action1<String> action) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action.onAction(s == null ? "" : s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    /*
        keyboard
     */

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View fView = view;
        fView.post(() -> {
            imm.hideSoftInputFromWindow(fView.getWindowToken(), 0);
            fView.clearFocus();
        });
    }

    public static void hideKeyboard(Activity activity, View rootView) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View fView = view;
        fView.post(() -> {
            imm.hideSoftInputFromWindow(fView.getWindowToken(), 0);
            fView.clearFocus();
            rootView.requestFocus();
        });
    }

    public static void showKeyboard(View view) {
        if (view == null) return;
        view.requestFocus();
        view.post(() -> {
            InputMethodManager imm = (InputMethodManager) view
                    .getContext()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        });
    }

    public static ViewTreeObserver.OnGlobalLayoutListener addOnShowKeyboardListener(
            Activity activity,
            View rootLayout,
            Action1<Boolean> action) {
        if (activity == null || rootLayout == null || action == null) return null;

        new Handler().postDelayed(() -> {
            int defaultKeyboardHeightDP = 100;
            Rect rect = new Rect();
            int estimatedKeyboardHeight = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    defaultKeyboardHeightDP + 48,
                    rootLayout.getResources().getDisplayMetrics());
            rootLayout.getWindowVisibleDisplayFrame(rect);
            int heightDiff = rootLayout.getRootView().getHeight() - (rect.bottom - rect.top);
            boolean isShown = heightDiff >= estimatedKeyboardHeight;
            action.onAction(isShown);
        }, 300);

        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + 48;
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        EstimatedKeyboardDP,
                        rootLayout.getResources().getDisplayMetrics());
                rootLayout.getWindowVisibleDisplayFrame(rect);
                int heightDiff = rootLayout.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;
                if (isShown == alreadyOpen) {
                    return;
                }
                alreadyOpen = isShown;
                action.onAction(isShown);
            }
        };
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        return listener;
    }


    public static void removeOnShowKeyboardListener(
            View rootLayout,
            ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (rootLayout == null || listener == null) return;
        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

}
