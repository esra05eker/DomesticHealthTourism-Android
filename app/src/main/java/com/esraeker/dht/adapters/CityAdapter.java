package com.esraeker.dht.adapters;

import android.content.Context;
import android.widget.TextView;

import com.esraeker.dht.entities.City;

public class CityAdapter extends SpinAdapter<City> {
    public CityAdapter(Context context) {
        super(context);
    }

    @Override
    void setContent(TextView txtRow, City item) {
        txtRow.setText(item.Name);
    }


}
