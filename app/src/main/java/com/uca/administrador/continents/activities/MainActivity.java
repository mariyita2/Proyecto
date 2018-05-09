package com.uca.administrador.continents.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tumblr.remember.Remember;
import com.uca.administrador.continents.R;
import com.uca.administrador.continents.adapters.ContinentAdapter;
import com.uca.administrador.continents.adapters.ContinentFromDatabaseAdapter;
import com.uca.administrador.continents.api.Api;
import com.uca.administrador.continents.models.ContinentsModel;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.uca.administrador.continents.receiver.NetworkStateChangeReceiver;

import static com.uca.administrador.continents.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

//import static com.uca.administrador.continents.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private static final String IS_FIRST_TIME = "is_first_time";
    private final String title = "Continentes del mundo";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
   private RecyclerView.LayoutManager mlayoutmanager;

//si lo vas aprobar va dar el clavo del layout ademas que con mi cel la probaria
    //porque dilata un mundo
    //QUE LE PASA AL LAYOUT?
    //te acordas que el fondo estaba morado y se fue el coso blanco?
    // y me preguntastes quee es eso?
    //mira lo del layout manager si eso va
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
       LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

               Snackbar.make(findViewById(R.id.activity_main), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
          }
        }, intentFilter);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CreateContinent.class));
                }
            });


            initViews();
            configureRecyclerView();

            if (!isFirstTime()) {
                getContinents();
                storeFirstTime();
            } else {
                getFromDataBase();
            }



    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }
//realm
    private void storeFirstTime() {
        Remember.putBoolean(IS_FIRST_TIME, true);
    }

    private boolean isFirstTime() {
        return Remember.getBoolean(IS_FIRST_TIME, false);
    }

//realm
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlayoutmanager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(!isConnected(MainActivity.this)){
                getContinents();
                }
                else{
                    getFromDataBase();
                }
            }
        });
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void getContinents() {
        Call<ArrayList<ContinentsModel>> call = Api.instance().getContinents();
        call.enqueue(new Callback<ArrayList<ContinentsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ContinentsModel>> call, Response<ArrayList<ContinentsModel>> response) {

                if (response.isSuccessful()) {

                    ContinentAdapter continentAdapter =new ContinentAdapter(response.body());
                    recyclerView.setAdapter(continentAdapter);

                    sync(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ContinentsModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.i("Debug: ", t.getMessage());
            }
        });
    }
//empieza realm
    private void sync(List<ContinentsModel> continentsModels) {
        for(ContinentsModel continentsModel : continentsModels) {
            store(continentsModel);
        }
    }


    private void store(ContinentsModel continentsModelFromApi) {
        String a=continentsModelFromApi.getId();
        if (exist(a)==false) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            ContinentsModel continentsModel = realm.createObject(ContinentsModel.class); // Create a new object

            continentsModel.setName(continentsModelFromApi.getName());
            continentsModel.setDescription(continentsModelFromApi.getDescription());
            realm.commitTransaction();
        }
    }

    private void getFromDataBase() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ContinentsModel> query = realm.where(ContinentsModel.class);

        RealmResults<ContinentsModel> results = query.findAll();

        mAdapter = new ContinentAdapter(results);
        recyclerView.setAdapter(mAdapter);
    }

    private boolean exist(String id){

        Boolean exist=false;
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<ContinentsModel> query = realm.where(ContinentsModel.class);

        RealmResults<ContinentsModel> results = query.findAll();

        for (int i=0; i<results.size(); i++)
        {
            if (id.equals(results.get(i).getId()))
            {
                exist=true;
            }
        }
        return exist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_continent clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
