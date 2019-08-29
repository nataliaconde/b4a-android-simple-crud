package com.back4app.parseobjectsimplecrud;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if desired
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com/")
                .build()
        );

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "gcmId");
        installation.saveInBackground();
    }
}
