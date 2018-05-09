package com.uca.administrador.continents.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uca.administrador.continents.R;
import com.uca.administrador.continents.holders.ContinentViewHolder;
import com.uca.administrador.continents.models.ContinentsModel;

import java.util.ArrayList;
import java.util.List;

public class ContinentAdapter extends RecyclerView.Adapter<ContinentViewHolder> {

    List<ContinentsModel> continents;

    public ContinentAdapter(List<ContinentsModel> continents) {
        this.continents = continents;
    }

    @Override
    public ContinentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_continent, parent, false);
        return new ContinentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContinentViewHolder holder, int position) {
        ContinentsModel continent = continents.get(position);

        holder.getId().setText(continent.getId());
        holder.getName().setText(continent.getName());
        holder.getDescription().setText(continent.getDescription());
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return continents.size();
    }
}
