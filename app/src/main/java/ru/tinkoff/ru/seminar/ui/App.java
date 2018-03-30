package ru.tinkoff.ru.seminar.ui;

import android.app.Application;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tinkoff.ru.seminar.api.Api;
import ru.tinkoff.ru.seminar.api.gson.deserializer.RatesDeserializer;
import ru.tinkoff.ru.seminar.api.model.RateObject;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createApi();
    }

    public Api getApi() {
        return api;
    }

    private void createApi() {
        api = new Retrofit.Builder()
                .baseUrl("http://api.fixer.io")
                .client(createOkHttpClient())
                .addConverterFactory(createGsonFactory())
                .build()
                .create(Api.class);
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private GsonConverterFactory createGsonFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapter(RateObject.class, new RatesDeserializer())
                        .create()
        );
    }
}