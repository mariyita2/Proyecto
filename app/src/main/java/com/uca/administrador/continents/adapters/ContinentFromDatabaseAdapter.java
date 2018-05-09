package com.uca.administrador.continents.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


import com.uca.administrador.continents.R;
import com.uca.administrador.continents.models.ContinentsModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by joel on 02/05/2018.
 */

public class ContinentFromDatabaseAdapter extends RecyclerView.Adapter<ContinentFromDatabaseAdapter.ViewHolder> {
    private RealmResults<ContinentsModel> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView name;
        public TextView description;
        public CardView item;
        public Context context;
        public ViewHolder(View view, Context _context) {
            super(view);

            name = view.findViewById(R.id.continent_name);
            description = view.findViewById(R.id.continent_description);
            item = view.findViewById(R.id.item);
            context = _context;
        }
    }



    public ContinentFromDatabaseAdapter(RealmResults<ContinentsModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ContinentFromDatabaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_continent, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ContinentFromDatabaseAdapter.ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final ContinentFromDatabaseAdapter.ViewHolder holder, final int position) {
        final ContinentsModel continentsModel = mDataset.get(position);

        Log.i("name", continentsModel.getName());
        Log.i("description", continentsModel.getName());


        holder.name.setText(continentsModel.getName());
        holder.description.setText(continentsModel.getDescription());



        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialDialog.Builder(holder.context)
                        .content("Desea borrar este registro.")
                        .positiveText("Borrar")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                remoteItemFromDatabase(mDataset.get(position));
                            }
                        })
                        .show();
                return true;
            }
        });

    }
    //Intala genymotions
    //quene es eso?
    //es para emular aplicaciones
    //es que cuando emulas te tira onde esta el error
    //tenes el instalador de eso?
    //creo que si
    //dejame los busco
    //si lo encontras los podes pegar de un solo al escritoio mio

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //aqui
    private void remoteItemFromDatabase(ContinentsModel continentsModel) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        continentsModel.deleteFromRealm();
        realm.commitTransaction();
    }
}
