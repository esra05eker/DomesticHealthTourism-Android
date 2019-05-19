package com.esraeker.dht.activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esraeker.dht.DhtApplication;
import com.esraeker.dht.R;
import com.esraeker.dht.entities.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    EditText txtTcNo, txtPassword;
    Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        setTitle (R.string.login);

        txtTcNo = findViewById (R.id.txtTcNo);
        txtPassword = findViewById (R.id.txtPassword);
        btnLogin = findViewById (R.id.btnLogin);
        tvRegister = findViewById (R.id.tvRegister);

        btnLogin.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty (txtTcNo.getText ( ).toString ( )) || TextUtils.isEmpty (txtPassword.getText ( ).toString ( ))) {
                    showToast (getString (R.string.enter_tc_password));
                } else if (txtTcNo.getText ( ).length ( ) != 11) {
                    txtTcNo.setError (getString (R.string.tc_must_be_11));
                } else {
                    Call<Patient> call = DhtApplication.getDbService ( ).login (txtTcNo.getText ( ).toString ( ), txtPassword.getText ( ).toString ( ));
                    call.enqueue (new Callback<Patient> ( ) {
                        @Override
                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                            Patient patient = response.body ( );
                            if (patient != null) {
                                saveLoggedInPatient (patient);
                                showToast (String.format (getString (R.string.hello_user), patient.Firstname, patient.Lastname));
                                startActivity (MainActivity.class);
                                finish ( );
                            }
                        }

                        @Override
                        public void onFailure(Call<Patient> call, Throwable t) {
                            showToast (getString (R.string.there_is_error) + "\n" + t.getMessage ( ));
                        }
                    });

                }
            }
        });

        tvRegister.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity (RegisterActivity.class);
            }
        });

    }
}
