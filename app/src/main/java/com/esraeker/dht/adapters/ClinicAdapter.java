package com.esraeker.dht.adapters;

import android.content.Context;
import android.widget.TextView;

import com.esraeker.dht.entities.Clinic;

public class ClinicAdapter extends SpinAdapter<Clinic> {
    public ClinicAdapter(Context context) {
        super(context);
    }

    @Override
    void setContent(TextView txtRow, Clinic item) {
        txtRow.setText(item.Name);
    }


}
