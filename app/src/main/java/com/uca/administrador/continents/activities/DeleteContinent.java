package com.uca.administrador.continents.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.uca.administrador.continents.R;
import com.uca.administrador.continents.api.Api;
import com.uca.administrador.continents.models.ContinentsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteContinent extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Context context = this;
        getExtras();
        deleteContinent(context);
        this.startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void getExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("Id");
        }
    }

    private void deleteContinent(final Context context){
        Call<ContinentsModel> call = Api.instance().deleteContinent(id);
        call.enqueue(new Callback<ContinentsModel>() {
            @Override
            public void onResponse(Call<ContinentsModel> call, Response<ContinentsModel> response) {
                if(response.body() != null){
                    Toast.makeText(context, "Elemento eliminado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContinentsModel> call, Throwable t) {
                Log.i("Debug: ", t.getMessage());

            }
        });
    }
}
