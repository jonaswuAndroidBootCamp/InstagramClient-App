package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

import android.app.Fragment;
import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/28.
 */
public abstract class BaseFragment extends Fragment implements APIDelegator, API.DataHandler {
    private EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        eventBus = new EventBus();
        super.onCreate(savedInstanceState);
    }


    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void onResume() {

        eventBus.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }
}
