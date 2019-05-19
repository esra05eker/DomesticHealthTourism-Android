package com.esraeker.dht.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.esraeker.dht.DhtApplication;
import com.esraeker.dht.R;
import com.esraeker.dht.entities.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    EditText txtTcNo, txtFirstname, txtLastname, txtEmail, txtPhone, txtPassword, txtRePassword;
    RadioGroup rgGender;

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        setTitle(getString(R.string.profile));

        txtTcNo = findViewById(R.id.txtTcNo);
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        rgGender = findViewById(R.id.rgGender);
        btnRegister = findViewById(R.id.btnRegister);


        Patient loggedInPatient = getLoggedInPatient();

        txtTcNo.setText(loggedInPatient.TcNo);
        txtFirstname.setText(loggedInPatient.Firstname);
        txtLastname.setText(loggedInPatient.Lastname);
        txtEmail.setText(loggedInPatient.Email);
        txtPhone.setText(loggedInPatient.Phone);
        rgGender.check(loggedInPatient.Gender == 1 ? R.id.radioFemale : R.id.radioMale);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(txtTcNo.getText().toString()) ||
                        TextUtils.isEmpty(txtFirstname.getText().toString()) ||
                        TextUtils.isEmpty(txtLastname.getText().toString()) ||
                        TextUtils.isEmpty(txtEmail.getText().toString()) ||
                        TextUtils.isEmpty(txtPhone.getText().toString()) ||
                        rgGender.getCheckedRadioButtonId() == -1 // Cinsiyet seçilmemiş ise
                ) {
                    showToast(getString(R.string.enter_all_fields));
                } else if (txtTcNo.getText().length() != 11) {
                    txtTcNo.setError(getString(R.string.tc_must_be_11));
                } else if (!txtPassword.getText().toString().equals(txtRePassword.getText().toString())) {
                    txtRePassword.setError(getString(R.string.passwords_not_matching));
                } else {
                    // Female radiobutonu seçilmiş ise gender 1, diğeri ise 2 dir
                    int gender = rgGender.getCheckedRadioButtonId() == R.id.radioFemale ? 1 : 2;

                    Patient patient = getLoggedInPatient();
                    patient.TcNo = txtTcNo.getText().toString();
                    patient.Firstname = txtFirstname.getText().toString();
                    patient.Lastname = txtLastname.getText().toString();
                    patient.Email = txtEmail.getText().toString();
                    patient.Phone = txtPhone.getText().toString();
                    patient.Gender = gender;
                    if (txtPassword.getText().length() > 0) {
                        patient.Password = txtPassword.getText().toString();
                    }

                    DhtApplication.getDbService().updateProfile(patient).enqueue(new Callback<Patient>() {
                        @Override
                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                            Patient patient = response.body();
                            if (patient != null) {
                                saveLoggedInPatient(patient);
                                showToast(getString(R.string.update_success));
                                finish();
                            } else {
                                showToast(response.raw().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<Patient> call, Throwable t) {

                        }
                    });

                }
            }
        });

    }
}
