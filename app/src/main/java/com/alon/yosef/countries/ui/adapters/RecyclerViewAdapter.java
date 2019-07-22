package com.alon.yosef.countries.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.alon.yosef.countries.R;
import com.alon.yosef.countries.service.model.Country;

/**
 * Created by alony on 18/07/2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Country> countries;

    private OnCountryListener onCountryListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnCountryListener onCountryListener;

        private TextView nameTV;
        private TextView nativeNameTV;
        private ViewHolder(View countryView, OnCountryListener onCountryListener) {
            super(countryView);
            this.onCountryListener = onCountryListener;
            nameTV = countryView.findViewById(R.id.nameTV);
            nativeNameTV = countryView.findViewById(R.id.nativeNameTV);
            countryView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCountryListener.onCountryClick(getAdapterPosition());
        }
    }

    public RecyclerViewAdapter(ArrayList<Country> countries, OnCountryListener onCountryListener) {
        this.countries = countries;
        this.onCountryListener = onCountryListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        return new ViewHolder(v, onCountryListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(countries.get(position).getName());
        holder.nativeNameTV.setText(countries.get(position).getNativeName());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public interface OnCountryListener {
        void onCountryClick(int position);
    }

    public ArrayList<Country> getCountries(){
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }
}
