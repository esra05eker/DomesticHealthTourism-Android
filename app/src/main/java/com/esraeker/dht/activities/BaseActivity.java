package com.esraeker.dht.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.esraeker.dht.DhtApplication;
import com.esraeker.dht.R;
import com.esraeker.dht.Utils;
import com.esraeker.dht.entities.Patient;
import com.google.gson.Gson;

public abstract class BaseActivity extends AppCompatActivity {

    // üstdeki bar
    ActionBar actionBar;

    // bizim app bilgilerimizi tutan custom application sınıfı
    DhtApplication dhtApp;
    // ayarlar
    SharedPreferences sharedPrefs;
    //Ayarları düzenlemeye yarayan editor
    SharedPreferences.Editor editor;
    // Servs url'si
    String url;
    // Giri yapmış kullanıcı
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DHT application
        dhtApp = ((DhtApplication) getApplication());

        // settings dosyasındaki uygulamamız içerisinde ayarları tuttuğumuz verileri okuyacağız
        sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE);

        // MVC projesinin çalıştığı url
        url = sharedPrefs.getString("url", null);

        // giriş yapmış hasta, eğer giriş yapılmamış ise null'dur
        patient = getLoggedInPatient();

        actionBar = getSupportActionBar();
        if (actionBar != null) {

            // splashactivity'de appbar'ımız yok fullscreen ekran
            if (this instanceof SplashActivity) {
                actionBar.hide(); // gizle
            }

            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
        }

    }

    // Belirtilen millisaniye sonra bir iş yap
    // Örneğin SplashActivity 3 saniye logoyu göster ve uygulamaya giriş yap
    void postDelayed(Runnable r, long delayMillis) {
        new Handler().postDelayed(r, delayMillis);
    }

    // Yeni activity başlat (yeni sayfa aç)
    void startActivity(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    // Toast mesajı göster
    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    // URL ayarlarını yap
    void setup() {
        // Kullanıcının ip adresini gireceği Dialog
        new MaterialDialog.Builder(this)
                .title(getString(R.string.server_ip_address))
                .content(getString(R.string.ip_dialog_content))
                .positiveText(getString(R.string.ok))
                .cancelable(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("IP", url, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, final CharSequence input) {

                        // network işlemlerini UI thread'te gerçekleştiremeyiz. o yüzden yeni thread oluşturuyoruz
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    url = input.toString();
                                    if (!url.startsWith("http://")) url = "http://" + url;
                                    if (!url.endsWith("/")) url = url + "/";

                                    Uri uri = Uri.parse(url);
                                    String host = uri.getHost();
                                    int port = uri.getPort() > 0 ? uri.getPort() : 80;

                                    if (Utils.isHostAvailable(host, port)) {
                                        editor = sharedPrefs.edit();
                                        editor.putString("url", url);
                                        editor.apply();
                                        dhtApp.buildClient();

                                        // UI işlemi yapıalcağı UI Thread'in de yapılmalı
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(SplashActivity.class);
                                                finish();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    // UI işlemi yapıalcağı UI Thread'in de yapılmalı
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(getString(R.string.ip_not_correct));
                                            setup();
                                        }
                                    });
                                }

                            }
                        }).start();


                    }
                }).show();
    }

    // giriş yapmış kullanıcı bilgilerini ayarlara kaydet
    void saveLoggedInPatient(Patient patient) {
        editor = sharedPrefs.edit();
        editor.putString("patient", new Gson().toJson(patient));
        editor.apply();
    }

    // giriş yapmış hastayı getir
    Patient getLoggedInPatient() {
        String patientJson = sharedPrefs.getString("patient", null);
        return new Gson().fromJson(patientJson, Patient.class);
    }

    // hasta (kullanıcı) giriş yapmış mı
    boolean isLoggedIn() {
        return getLoggedInPatient() != null;
    }

    // Sağ üstteki 3 nokta menü ayarları
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isLoggedIn())
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // menüden seçilen seçeneğe göre açılacak sayfaları ayarlıyoruz
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMakeAppoinment) {
            // Ana sayfa
            if (!(this instanceof MainActivity)) startActivity(MainActivity.class);
        } else if (id == R.id.menuMyAppoinments) {
            // Randevularım sayfası
            if (!(this instanceof MyAppoinments)) startActivity(MyAppoinments.class);
        } else if (id == R.id.menuProfile) {
            // Profilime sayfası
            if (!(this instanceof ProfileActivity)) startActivity(ProfileActivity.class);
        } else if (id == R.id.menuLogout) {
            editor = sharedPrefs.edit();
            editor.remove("patient");
            editor.apply();
            startActivity(SplashActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

}
