package io.oasisbloc.wallet.model.repository;

import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import io.oasisbloc.wallet.App;

public class PreferenceRepository {

    public static SharedPreferences create(String name) {
        try {
            return EncryptedSharedPreferences.create(
                    name,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    App.getInstance(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
