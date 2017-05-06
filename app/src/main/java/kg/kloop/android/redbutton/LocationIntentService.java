package kg.kloop.android.redbutton;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class LocationIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
