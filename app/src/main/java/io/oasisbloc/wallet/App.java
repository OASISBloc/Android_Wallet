package io.oasisbloc.wallet;

import androidx.multidex.MultiDexApplication;

import io.oasisbloc.wallet.model.repository.remote.RemoteRepository;

public class App extends MultiDexApplication {

    private static App APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;

        RemoteRepository.init();
    }

    public static App getInstance() {
        return APP;
    }


    /*
        Build
     */

    public static String getAppId() {
        return BuildConfig.APPLICATION_ID;
    }

    public static boolean isRelease() {
        return !BuildConfig.DEBUG;
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static String getVersion() {
        return "v" + BuildConfig.VERSION_NAME;
    }

    public static boolean isProduction() {
        //noinspection ConstantConditions
        return BuildConfig.FLAVOR.contains("production");
    }
}
