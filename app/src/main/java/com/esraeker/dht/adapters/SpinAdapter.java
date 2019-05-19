package com.esraeker.dht.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.esraeker.dht.R;

import java.util.ArrayList;
import java.util.List;

public abstract class SpinAdapter<T> extends ArrayAdapter<T> implements SpinnerAdapter {
    SpinAdapter(Context context) {
        super(context, R.layout.row_spinner, new ArrayList<T>());
    }

    SpinAdapter(Context context, List<T> items) {
        super(context, R.layout.row_spinner, items);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) { // eğer ilk değer ise
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection(boolean dropdown) {
        // Eğer ilk seçenek ise "Seçin" şeklinde yazı göster
        TextView view = new TextView(getContext());
        view.setText(R.string.select);
        view.setPadding(0, 20, 0, 10);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    // adapter her bir eleman için göstereceği arayüz ayarlanıyor
    View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View layout = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.row_spinner, parent, false);


        position = position - 1; // Adjust for initial selection item
        T item = getItem(position);
        TextView txtRow = layout.findViewById(R.id.txtRow);
        if (item != null) {
            setContent(txtRow, item);
        }


        return layout;
    }

    abstract void setContent(TextView txtRow, T item);

    public void addNewItems(List<T> items) {
        if (items == null) return;
        clear();
        addAll(items);
        notifyDataSetChanged();
    }
}
