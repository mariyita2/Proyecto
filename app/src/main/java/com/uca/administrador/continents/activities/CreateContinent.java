package com.uca.administrador.continents.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.uca.administrador.continents.R;
import com.uca.administrador.continents.api.Api;
import com.uca.administrador.continents.models.ContinentsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContinent extends AppCompatActivity {

    private final String title = "Crear un Continente";
    private ContinentsModel continent;
    private EditText name;
    private EditText description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        initializeViews();
    }

    private void validateData(){
        if (name.getText().toString().trim().isEmpty() & description.getText().toString().trim().isEmpty()) {
            name.setError("This field can not be blank");
            description.setError("This field can not be blank");
        }

        if(!name.getText().toString().trim().isEmpty()){
            if(!description.getText().toString().trim().isEmpty()){
                createProduct();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                description.setError("This field can not be blank");
            }
        } else {
            name.setError("This field can not be blank");
        }
    }

    public void initializeViews () {
        name = findViewById(R.id.continent_name);
        description = findViewById(R.id.continent_description);
    }

    public void createProduct() {
        continent = new ContinentsModel();

        continent.setName(name.getText().toString());
        continent.setDescription(description.getText().toString());

        Call<ContinentsModel> call = Api.instance().createContinent(continent);
        call.enqueue(new Callback<ContinentsModel>() {
            @Override
            public void onResponse(@NonNull Call<ContinentsModel> call, @NonNull Response<ContinentsModel> response) {
                if(response.body() != null){
                    try {

                        assert response != null;
                        assert response.body() != null;

                    }catch(NullPointerException e){
                        Log.i("Debug: ", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContinentsModel> call, @NonNull Throwable t) {
                Log.i("Debug: ", t.getMessage());
            }
        });


    }

    public void acceptOnclick(View view) {
        validateData();
    }

    public void cancelOnclick(View view) {
        finish();
    }
}
