package in.projectmanas.android.hub;


import android.app.Application;

import com.aspirephile.shared.debug.Logger;
import com.parse.Parse;

public class App extends Application {
    Logger l = new Logger(App.class);

    @Override
    public void onCreate() {
        super.onCreate();
        l.d(App.class.getSimpleName() + " being created");
        Parse.initialize(
                new Parse.Configuration.Builder(this)
                        .applicationId(Constants.parse.applicationId)
                        .clientKey(Constants.parse.clientKey)
                        .server(Constants.parse.server)
                        .build()
        );
        //LaundroDb.initialize(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        l.w("onTerminate");
    }
}
