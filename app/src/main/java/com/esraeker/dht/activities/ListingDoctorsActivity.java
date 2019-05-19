package com.esraeker.dht.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.esraeker.dht.DhtApplication;
import com.esraeker.dht.R;
import com.esraeker.dht.adapters.AppoinmentAdapter;
import com.esraeker.dht.entities.Appoinment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingDoctorsActivity extends BaseActivity {

    ListView lstAppoinments;

    AppoinmentAdapter appoinmentAdapter;

    int doctorId;
    String appoinmentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_doctors);
        setTitle(R.string.appoinments);

        // Adapter initialize et
        appoinmentAdapter = new AppoinmentAdapter(this, R.layout.row_appoinment_list, new ArrayList<Appoinment>());

        lstAppoinments = findViewById(R.id.lstAppoinments);
        lstAppoinments.setAdapter(appoinmentAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doctorId = extras.getInt("doctorId");
            appoinmentDate = extras.getString("date");
            fillAppoinmentList(doctorId, appoinmentDate);
        } else {
            showToast(getString(R.string.select_doctor_and_date));
            finish();
        }


        lstAppoinments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appoinment appoinment = appoinmentAdapter.getItem(position);
                if (appoinment != null) {
                    if (!appoinment.isSuitable) {
                        showToast(getString(R.string.appoinment_time_not_suitable));
                    } else {
                        DhtApplication.getDbService().makeAppoinment(doctorId, getLoggedInPatient().Id, appoinmentDate + " " + appoinment.Time).enqueue(new Callback<Appoinment>() {
                            @Override
                            public void onResponse(Call<Appoinment> call, Response<Appoinment> response) {
                                Appoinment made_appoinment = response.body();
                                if (made_appoinment != null) {
                                    showToast(getString(R.string.appoinment_success));
                                    startActivity(MyAppoinments.class);
                                } else {
                                    showToast(getString(R.string.try_again));
                                }
                            }

                            @Override
                            public void onFailure(Call<Appoinment> call, Throwable t) {

                            }
                        });
                    }
                }


            }
        });

    }

    void fillAppoinmentList(int doctorId, String appoinmentDate) {
        DhtApplication.getDbService().searchAppoinment(doctorId, appoinmentDate).enqueue(new Callback<List<Appoinment>>() {
            @Override
            public void onResponse(Call<List<Appoinment>> call, Response<List<Appoinment>> response) {
                List<Appoinment> appoinments = response.body();
                appoinmentAdapter.addNewItems(appoinments);
            }

            @Override
            public void onFailure(Call<List<Appoinment>> call, Throwable t) {

            }
        });
    }
}
