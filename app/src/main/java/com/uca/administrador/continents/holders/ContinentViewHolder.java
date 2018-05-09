package com.uca.administrador.continents.holders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uca.administrador.continents.R;
import com.uca.administrador.continents.activities.DeleteContinent;
import com.uca.administrador.continents.activities.UpdateContinent;

public class ContinentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView id;
    private TextView name;
    private TextView description;
    private ImageButton delete;
    private ImageButton edit;

    private Context context;
    private Intent intent;

    public ContinentViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        setId((TextView) itemView.findViewById(R.id.continent_id));
        setName((TextView) itemView.findViewById(R.id.continent_name));
        setDescription((TextView) itemView.findViewById(R.id.continent_description));
        setDelete((ImageButton) itemView.findViewById(R.id.btn_delete));
        setEdit((ImageButton) itemView.findViewById(R.id.btn_edit));

    }

    public void setOnClickListeners(){
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_delete:
                    AlertDialog dialog = deleteDialog();
                    dialog.show();
                break;
            case R.id.btn_edit:
                intent = new Intent(context, UpdateContinent.class);
                intent.putExtra("Id", id.getText().toString());
                intent.putExtra("Name", name.getText().toString());
                intent.putExtra("Description", description.getText().toString());
                context.startActivity(intent);
                break;
        }
    }

    public AlertDialog deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Eliminar Continente")
                .setMessage("El elemento será eliminado")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent = new Intent(context, DeleteContinent.class);
                                intent.putExtra("Id", id.getText().toString());
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    public TextView getId() {
        return id;
    }

    public void setId(TextView id) {
        this.id = id;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public ImageButton getDelete() {
        return delete;
    }

    public void setDelete(ImageButton delete) {
        this.delete = delete;
    }

    public ImageButton getEdit() {
        return edit;
    }

    public void setEdit(ImageButton edit) {
        this.edit = edit;
    }

}
