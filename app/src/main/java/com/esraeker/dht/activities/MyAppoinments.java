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

public class MyAppoinments extends BaseActivity {

    ListView lstAppoinments;

    AppoinmentAdapter appoinmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_doctors);
        setTitle(R.string.my_appoinments);

        // Adapter initialize et
        appoinmentAdapter = new AppoinmentAdapter(this, R.layout.row_appoinment_list, new ArrayList<Appoinment>());

        lstAppoinments = findViewById(R.id.lstAppoinments);
        lstAppoinments.setAdapter(appoinmentAdapter);


        lstAppoinments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appoinment appoinment = appoinmentAdapter.getItem(position);
                if (appoinment != null) {
                    if (appoinment.isSuitableForCancel) {
                        DhtApplication.getDbService().cancelAppoinment(appoinment.Id).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                boolean is_canceled = response.body();
                                if (is_canceled) {
                                    showToast(getString(R.string.appoinment_is_canceled));
                                    // yeniden yükle
                                    fillMyAppoinmentList();
                                } else {
                                    showToast(getString(R.string.appoinment_not_cancelled));
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });

                    } else {
                        showToast(getString(R.string.past_appoinment_cant_cancel));
                    }
                }
            }
        });
        fillMyAppoinmentList();
    }

    void fillMyAppoinmentList() {
        DhtApplication.getDbService().myAppoinments(getLoggedInPatient().Id).enqueue(new Callback<List<Appoinment>>() {
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
