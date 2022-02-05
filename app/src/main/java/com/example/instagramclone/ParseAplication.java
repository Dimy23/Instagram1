package com.example.instagramclone;

import android.app.Application;
import com.parse.ParseObject;
import com.parse.Parse;

public class ParseAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass( Post.class );
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("IRXtybLIL68cXwCYPmfSbyf7SyxqErPKHOQRdBOn")
                .clientKey("cKxHwaWLNafOHzvFyieGvEVlpOnNlphw919lV6HD")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
