package com.esraeker.dht;

import android.app.Application;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DhtApplication extends Application {
    // veritabanı ile işlem yapacak servis
    private static DbService dbService;

    public DhtApplication() {

    }

    public static DbService getDbService() {
        return dbService;
    }

    public void buildClient() {
        // servis url'sini ayarlardan oku
        String url = readUrl();
        // Singleton Retfofit instance
        Retrofit client = new Retrofit.Builder()
                .baseUrl(url + "mobile/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbService = client.create(DbService.class);
    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences("settings", MODE_PRIVATE);
    }

    // servis url'sini oku
    public String readUrl() {
        return getPreferences().getString("url", null);
    }

}
