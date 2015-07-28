package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.greenrobot.event.EventBus;


/**
 * Created by jonaswu on 2015/7/28.
 */
public abstract class BaseFragmentActivity extends SherlockFragmentActivity implements APIDelegator, API.DataHandler {

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventBus = new EventBus();
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        eventBus.register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

}
