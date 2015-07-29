package androidcourse.android.jonaswu.yahoo.com.instagramclient.lib;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.R;
import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/28.
 */
public class API {


    public String identifier;

    public API(String identifier) {
        this.identifier = identifier;
    }

    public API() {
        this.identifier = "";
    }

    public interface DataHandler {
        public void onEventMainThread(API.ReturnDataEvent dma);
    }

    public void getLastestCommentById(APIDelegator apiDelegator, String mediaId) {
        EventBus eventBus = apiDelegator.getEventBus();
        Context ctx = apiDelegator.getContext();
        HashMap<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("access_token", ctx.getString(R.string.access_token));
        try {
            BaseAsyncTask task = new getDataUrlParams(ctx, String.format("v1/media/%s/comments", mediaId), this.identifier, urlParams, eventBus);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPopular(APIDelegator apiDelegator) {
        EventBus eventBus = apiDelegator.getEventBus();
        Context ctx = apiDelegator.getContext();
        HashMap<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("access_token", ctx.getString(R.string.access_token));
        try {
            BaseAsyncTask task = new getDataUrlParams(ctx, "v1/media/popular", this.identifier, urlParams, eventBus);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getDataUrlParams extends BaseAsyncTask {

        private final String identifier;
        private String path;
        private HashMap<String, Object> data;

        public getDataUrlParams(Context context, String path, String identifier, HashMap<String, Object> data, EventBus localBus) {
            super(context, localBus);
            this.path = path;
            this.data = data;
            this.identifier = identifier;
        }


        @Override
        public void my_doInBackground(Object... params) {
            try {
                ReturnDataEvent returnDataEvent;
                returnDataEvent = new ReturnDataEvent(WebClient.basicHTTPGet(context, data, this.path, localBus), this.identifier);
                Log.e("pre receive data", returnDataEvent.data.toString());
                localBus.post(returnDataEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class ReturnDataEvent {
        public JSONObject data;
        public String identifier;

        public ReturnDataEvent(JSONObject data, String identifier) {
            this.data = data;
            this.identifier = identifier;
        }
    }

    /**
     * Created by jonaswu on 2015/7/28.
     */
    public static interface APIDelegator {
        public EventBus getEventBus();

        public Context getContext();
    }
}
