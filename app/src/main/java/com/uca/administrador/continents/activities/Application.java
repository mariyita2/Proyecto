package com.uca.administrador.continents.activities;

import com.tumblr.remember.Remember;

import io.realm.Realm;

/**
 * Created by joel on 02/05/2018.
 */

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Remember.init(getApplicationContext(), "com.uca.administrador.continents");
    }
}
//en los layout a uno le puse el coso que hace que sincronize nadamas
//ESTA MAL PUESTO
//xd