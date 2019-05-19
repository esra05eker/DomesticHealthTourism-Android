package com.esraeker.dht.activities;

import android.net.Uri;
import android.os.Bundle;

import com.esraeker.dht.R;
import com.esraeker.dht.Utils;

import java.io.IOException;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash);

        if (actionBar != null) {
            actionBar.hide ( );
        }

        new Thread (new Runnable ( ) {
            @Override
            public void run() {

                try {
                    if (url != null) {
                        // servis url'sini test et
                        Uri uri = Uri.parse (url);
                        String host = uri.getHost ( );
                        int port = uri.getPort ( ) > 0 ? uri.getPort ( ) : 80;
                        boolean checkurl = Utils.isHostAvailable (host, port);
                    }
                } catch (IOException e) {
                    // herhangi bir hata oluşursa servis url'si ayarlama dialoğunu tekrar göster
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            setup ( );
                        }
                    });
                }

                // boşsa veya ulaşılabilir değil ise servis url 'si ayarlama setup dialogunu göster
                if (url == null || url.equals ("")) {
                    // görsel işlemler UI thread'in yapılması gerekir o yüzden runOnUiThread metodu içerisinde çalıştırıyoruz
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            setup ( );
                        }
                    });
                } else {
                    // UI thread'inde işlem yap
                    runOnUiThread (new Runnable ( ) {
                        @Override
                        public void run() {
                            // zaman gecikmeli işlem yap 3 saniye sonra yeni sayfaya geç
                            // splash screen mantığı
                            postDelayed (new Runnable ( ) {
                                @Override
                                public void run() {
                                    // servis url'si göster
                                    showToast (String.format (getString (R.string.system_running_on_url), url));
                                    dhtApp.buildClient ( );
                                    if (!isLoggedIn ( )) {
                                        startActivity (LoginActivity.class);
                                    } else {
                                        startActivity (MainActivity.class);
                                    }

                                    finish ( );
                                }
                            }, 3000);
                        }
                    });

                }
            }
        }).start ( );

    }


}
