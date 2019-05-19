package com.esraeker.dht.adapters;

import android.content.Context;
import android.widget.TextView;

import com.esraeker.dht.entities.Hospital;

public class HospitalAdapter extends SpinAdapter<Hospital> {
    public HospitalAdapter(Context context) {
        super(context);
    }

    @Override
    void setContent(TextView txtRow, Hospital item) {
        txtRow.setText(item.Name);
    }


}
