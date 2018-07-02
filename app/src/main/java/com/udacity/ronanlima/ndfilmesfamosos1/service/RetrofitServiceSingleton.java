package com.udacity.ronanlima.ndfilmesfamosos1.service;

import android.util.Log;

import com.google.gson.Gson;
import com.udacity.ronanlima.ndfilmesfamosos1.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rlima on 29/06/18.
 */

public class RetrofitServiceSingleton {
    public static final String TAG = RetrofitServiceSingleton.class.getCanonicalName().toUpperCase();

    private static final String BASE_URL = BuildConfig.BASE_URL_TMDB;
    public static RetrofitServiceSingleton instance;
    private Retrofit retrofit;

    public static synchronized RetrofitServiceSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitServiceSingleton();
        }
        return instance;
    }

    public RetrofitServiceSingleton() {
        setRetrofit(createService());
    }

    private Retrofit createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.readTimeout(30, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(new LogInterceptor());
        return okHttpClient.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    class LogInterceptor implements okhttp3.Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(TAG, String.format("Enviando requisição para o serviço. Url = %s, conexão = %s, cabeçalho= %s", request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d(TAG, String.format("Resposta recebida da url %s em %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

}
