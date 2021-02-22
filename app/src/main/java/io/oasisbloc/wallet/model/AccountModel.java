package io.oasisbloc.wallet.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.oasisbloc.wallet.data.KeyPair;
import io.oasisbloc.wallet.model.repository.PreferenceRepository;
import io.oasisbloc.wallet.model.repository.remote.RemoteException;
import io.oasisbloc.wallet.model.repository.remote.RemoteRepository;
import io.oasisbloc.wallet.model.repository.remote.RemoteResult;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AccountModel {

    /*Secure Area*/
}
