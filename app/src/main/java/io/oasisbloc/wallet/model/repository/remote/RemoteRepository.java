package io.oasisbloc.wallet.model.repository.remote;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRepository {

    private static Retrofit retrofit;

    public static void init() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(App.isDebug()
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        retrofit = new Retrofit.Builder()
                .baseUrl(App.getInstance().getString(R.string.const_api_url))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(loggingInterceptor)
                        .build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
