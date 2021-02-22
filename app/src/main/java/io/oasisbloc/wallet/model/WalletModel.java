package io.oasisbloc.wallet.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.oasisbloc.wallet.data.Account;
import io.oasisbloc.wallet.data.LockupHistory;
import io.oasisbloc.wallet.data.Token;
import io.oasisbloc.wallet.data.TokenResources;
import io.oasisbloc.wallet.data.Transaction;
import io.oasisbloc.wallet.data.TransactionHistory;
import io.oasisbloc.wallet.model.repository.remote.LockupHistoryRemoteResult;
import io.oasisbloc.wallet.model.repository.remote.RemoteException;
import io.oasisbloc.wallet.model.repository.remote.RemoteRepository;
import io.oasisbloc.wallet.model.repository.remote.RemoteResult;
import io.oasisbloc.wallet.util.AESUtil;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public final class WalletModel {

    /*Secure Area*/
}