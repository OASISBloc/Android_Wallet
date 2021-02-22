package io.oasisbloc.wallet.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.model.DeviceModel;
import io.oasisbloc.wallet.ui.setting.AppLockActivity;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static boolean INNER_ACTIVITY_START = false;
    private boolean mShowAppLock = false;
    private boolean mAppLockEnabled = true;

    @Override
    protected void onStart() {
        super.onStart();
        INNER_ACTIVITY_START = true;
        if (DeviceModel.isAppLockEnabled() && mAppLockEnabled) {
            if (mShowAppLock) {
                mShowAppLock = false;
                AppLockActivity.startForChecking(this);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DeviceModel.isAppLockEnabled() && mAppLockEnabled) {
            if (!INNER_ACTIVITY_START) {
                mShowAppLock = true;
            }
        }
        INNER_ACTIVITY_START = false;
    }

    public void disableAppLockPace() {
        mAppLockEnabled = false;
    }


    /*
       activity
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionBarBackButtonEnable() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void actionBarBackButtonEnable(@DrawableRes int id) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(id);
        }
    }


    /*
        action
     */

    public void launchWebUrl(String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchEmailApp(String to, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String data = "mailto:" + to + "?subject=" + subject + "&body=" + body;
        intent.setData(Uri.parse(data));
        startActivity(Intent.createChooser(intent, ""));
    }

    public void launchUpdater() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + App.getAppId()));
        intent.setPackage("com.android.vending");
        startActivity(Intent.createChooser(intent, ""));
    }

    public void launchShare(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, ""));
    }


    /*
        Toast
     */

    private static Toast TOAST;

    public void showToast(@StringRes int id) {
        showToast(getString(id));
    }

    public void showToast(Throwable throwable) {
        showToast(ExceptionUtils.getMessage(throwable));
    }

    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (TOAST == null) {
                TOAST = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            } else {
                TOAST.setText(msg);
            }
            TOAST.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastAndFinish(Throwable throwable) {
        showToast(throwable);
        finish();
    }


    /*
        Progress
     */

    private Dialog mProgressDialog;

    public void handleProgressDialog(boolean show) {
        if (show) showProgressDialog();
        else dismissProgressDialog();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            try {
                mProgressDialog = new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setView(new ProgressBar(this, null, android.R.attr.progressBarStyleLarge))
                        .create();

                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window window = mProgressDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                }

                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
