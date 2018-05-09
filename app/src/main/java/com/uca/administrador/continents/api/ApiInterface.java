package com.uca.administrador.continents.api;


import com.uca.administrador.continents.models.ContinentsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET ("Continents")
    Call<ArrayList<ContinentsModel>> getContinents();

    @POST ("Continents")
    Call<ContinentsModel> createContinent(@Body ContinentsModel continentsModel);

    @PUT ("Continents/{id}")
    Call<ContinentsModel> updateContinent(@Path("id") String id, @Body ContinentsModel continentsModel);

    @DELETE ("Continents/{id}")
    Call<ContinentsModel> deleteContinent(@Path("id") String id);

}
