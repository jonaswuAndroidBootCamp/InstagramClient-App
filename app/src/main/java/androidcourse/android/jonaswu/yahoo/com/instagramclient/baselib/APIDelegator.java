package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

import android.content.Context;

import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/28.
 */
public interface APIDelegator {
    public EventBus getEventBus();

    public Context getContext();
}
