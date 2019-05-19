package com.esraeker.dht.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.esraeker.dht.R;
import com.esraeker.dht.entities.Appoinment;

import java.util.List;

public class AppoinmentAdapter extends ArrayAdapter<Appoinment> {
    public AppoinmentAdapter(@NonNull Context context, int resource, @NonNull List<Appoinment> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // her appoinent için gösterilecek elemenın arayüzü ayarlanıyor
    View getCustomView(int position, View convertView, ViewGroup parent) {
        View layout = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.row_appoinment_list, parent, false);

        Appoinment item = getItem(position);
        TextView txtDate = layout.findViewById(R.id.txtDate);
        TextView txtDoctor = layout.findViewById(R.id.txtDoctor);
        TextView txtTime = layout.findViewById(R.id.txtTime);
        TextView txtHospital = layout.findViewById(R.id.txtHospital);
        TextView txtClinic = layout.findViewById(R.id.txtClinic);

        if (item != null) {
            txtDate.setText(item.Date);
            txtDoctor.setText(item.DoctorName);
            txtTime.setText(item.Time);
            txtHospital.setText(item.HospitalName);
            txtClinic.setText(item.ClinicName);

            if (!item.isSuitable)
                txtTime.setTextColor(Color.RED);
            else
                txtTime.setTextColor(Color.GREEN);

        }


        return layout;
    }

    public void addNewItems(List<Appoinment> items) {
        if (items == null) return;
        clear();
        addAll(items);
        notifyDataSetChanged();
    }

}
