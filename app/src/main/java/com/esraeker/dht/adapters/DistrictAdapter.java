package com.esraeker.dht.adapters;

import android.content.Context;
import android.widget.TextView;

import com.esraeker.dht.entities.District;

public class DistrictAdapter extends SpinAdapter<District> {
    public DistrictAdapter(Context context) {
        super(context);
    }

    @Override
    void setContent(TextView txtRow, District item) {
        txtRow.setText(item.Name);
    }


}
