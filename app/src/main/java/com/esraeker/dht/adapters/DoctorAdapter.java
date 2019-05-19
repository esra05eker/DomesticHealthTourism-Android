package com.esraeker.dht.adapters;

import android.content.Context;
import android.widget.TextView;

import com.esraeker.dht.entities.Doctor;

public class DoctorAdapter extends SpinAdapter<Doctor> {
    public DoctorAdapter(Context context) {
        super(context);
    }

    @Override
    void setContent(TextView txtRow, Doctor item) {
        txtRow.setText(item.Name);
    }


}
