package io.oasisbloc.wallet.ui.setting.viewmodel;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.model.DeviceModel;

public class AppLockViewModel extends BaseViewModel {

    public enum Route {
        PHONE_HOME, BACK_ACTIVITY, CANCEL_DIALOG
    }

    public enum Step {
        REGISTER, CHECK, BLOCK,
    }

    private Context mContext;
    private boolean mIsSettingMode;

    private Step mStep;
    private String mCode1 = "";
    private String mCode2 = "";
    private CountDownTimer mTimer;

    private MutableLiveData<Step> stepLiveData = new MutableLiveData<>();
    private MutableLiveData<Route> routeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> toastLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> logoLiveData = new MutableLiveData<>();
    private MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<String> blockTimerLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> codeCountLiveData = new MutableLiveData<>();


    public static AppLockViewModel get(FragmentActivity activity) {
        AppLockViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AppLockViewModel(activity);
            }
        }).get(AppLockViewModel.class);
        activity.getLifecycle().addObserver(vm);
        return vm;
    }

    private AppLockViewModel(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        startBlockTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopBlockTimer();
    }

    public void setSettingMode() {
        mIsSettingMode = true;
        initMode();
    }

    public void setCheckingMode() {
        mIsSettingMode = false;
        initMode();
    }

    private void initMode() {
        if (mIsSettingMode) {
            if (DeviceModel.isAppLockEnabled()) {
                if (DeviceModel.isAppLockBlocking()) {
                    setStep(Step.BLOCK);
                } else {
                    setStep(Step.CHECK);
                }
            } else {
                setStep(Step.REGISTER);
            }
        } else {
            if (DeviceModel.isAppLockFirstLaunch()) {
                setStep(Step.REGISTER);
            } else if (DeviceModel.isAppLockEnabled()) {
                if (DeviceModel.isAppLockBlocking()) setStep(Step.BLOCK);
                else setStep(Step.CHECK);
            } else {
                routeLiveData.setValue(Route.BACK_ACTIVITY);
            }
        }
    }

    private void setStep(Step step) {
        if (mStep != step) {
            mStep = step;
            stepLiveData.setValue(step);
        }
        setState();
    }

    private void setState() {
        if (mStep == Step.REGISTER) {
            logoLiveData.setValue(R.drawable.pin_logo_normal);
            blockTimerLiveData.setValue("");

            if (mCode1.length() < 4) {
                codeCountLiveData.setValue(mCode1.length());
                titleLiveData.setValue(mContext.getString(R.string.applock_title_enter));
                messageLiveData.setValue(mContext.getString(R.string.applock_msg_edit));
                errorLiveData.setValue("");
            } else {
                codeCountLiveData.setValue(mCode2.length());
                titleLiveData.setValue(mContext.getString(R.string.applock_title_check));

                if (mCode2.length() >= 4) {
                    if (mCode1.equals(mCode2)) {
                        DeviceModel.setAppLockCode(mCode1);
                        DeviceModel.setAppLockEnable(true);

                        DeviceModel.setAppLockFirstLaunch(false);  //20191015 yskim modified

                        toastLiveData.setValue(mContext.getString(R.string.applock_msg_activated));
                        routeLiveData.setValue(Route.BACK_ACTIVITY);

                    } else {
                        mCode2 = "";
                        codeCountLiveData.setValue(0);
                        messageLiveData.setValue("");
                        errorLiveData.setValue(mContext.getString(R.string.applock_msg_not_matched));
                    }
                }
            }

        } else if (mStep == Step.CHECK) {
            logoLiveData.setValue(R.drawable.pin_logo_normal);
            titleLiveData.setValue(mContext.getString(R.string.applock_title_enter));
            messageLiveData.setValue("");
            blockTimerLiveData.setValue("");

            if (mCode1.length() >= 4) {
                if (mCode1.equals(DeviceModel.getAppLockCode())) {
                    if (mIsSettingMode) {
                        DeviceModel.setAppLockEnable(false);
                    }
                    DeviceModel.clearAppLockErrorCount();
                    routeLiveData.setValue(Route.BACK_ACTIVITY);
                } else {
                    mCode1 = "";
                    if (mIsSettingMode) {
                        errorLiveData.setValue(mContext.getString(R.string.applock_msg_wrong));
                    } else {
                        DeviceModel.addAppLockErrorCount();
                        if (DeviceModel.isAppLockBlocking()) {
                            DeviceModel.updateAppLockBlockTime();
                            setStep(Step.BLOCK);
                        } else {
                            int now = DeviceModel.getAppLockErrorNowCount();
                            int max = DeviceModel.getAppLockErrorMaxCount();
                            String msg = mContext.getString(R.string.applock_msg_wrong_format, now, max);
                            errorLiveData.setValue(msg);
                        }
                    }
                }
            }
            codeCountLiveData.setValue(mCode1.length());

        } else if (mStep == Step.BLOCK) {
            logoLiveData.setValue(R.drawable.pin_logo_error);
            titleLiveData.setValue(mContext.getString(R.string.applock_title_block));
            messageLiveData.setValue(mContext.getString(R.string.applock_msg_block));
            errorLiveData.setValue("");
            blockTimerLiveData.setValue(getBlockTimerText());
            if (DeviceModel.isAppLockBlockRemindTimeExpired()) {
                DeviceModel.clearAppLockErrorCount();
                setStep(Step.CHECK);
            }
        }
    }

    private void startBlockTimer() {
        stopBlockTimer();
        mTimer = new CountDownTimer(Long.MAX_VALUE, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mStep == Step.BLOCK) {
                    setStep(Step.BLOCK);
                }
            }

            @Override
            public void onFinish() {
            }
        };
        mTimer.start();
    }

    private void stopBlockTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private String getBlockTimerText() {
        long remind = DeviceModel.getAppLockBlockRemindTimeSecond();
        return String.format(
                Locale.getDefault(),
                "%02d:%02d",
                remind / 60,
                remind % 60);
    }

    /*
        getter
     */

    public MutableLiveData<Step> getStepLiveData() {
        return stepLiveData;
    }

    public MutableLiveData<Route> getRouteLiveData() {
        return routeLiveData;
    }

    public MutableLiveData<Integer> getCodeCountLiveData() {
        return codeCountLiveData;
    }

    public MutableLiveData<Integer> getLogoLiveData() {
        return logoLiveData;
    }

    public MutableLiveData<String> getTitleLiveData() {
        return titleLiveData;
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<String> getBlockTimerLiveData() {
        return blockTimerLiveData;
    }

    public MutableLiveData<String> getToastLiveData() {
        return toastLiveData;
    }


    /*
        action
     */

    public void onBackPressed() {
        switch (mStep) {
            case BLOCK:
                routeLiveData.setValue(Route.PHONE_HOME);
                break;
            case CHECK:
                routeLiveData.setValue(mIsSettingMode ? Route.BACK_ACTIVITY : Route.PHONE_HOME);
                break;
            case REGISTER:
                routeLiveData.setValue(Route.CANCEL_DIALOG);
                break;
        }
    }

    public void cancel() {
        DeviceModel.setAppLockFirstLaunch(false);
        DeviceModel.setAppLockEnable(false);
        routeLiveData.setValue(Route.BACK_ACTIVITY);
    }

    public void appendCode(int number) {
        if (mIsSettingMode) {
            if (mCode1.length() < 4) {
                mCode1 = mCode1 + number;
            } else if (mCode2.length() < 4) {
                mCode2 = mCode2 + number;
            }
        } else {
            if (mCode1.length() < 4) {
                mCode1 = mCode1 + number;
            }
        }
        setState();
    }

    public void removeCode() {
        if (mIsSettingMode) {
            if (mCode1.length() < 4) {
                mCode1 = mCode1.substring(0, Math.max(mCode1.length() - 1, 0));
            } else {
                mCode2 = mCode2.substring(0, Math.max(mCode2.length() - 1, 0));
            }
        } else {
            mCode1 = mCode1.substring(0, Math.max(mCode1.length() - 1, 0));
        }
        setState();
    }
}
