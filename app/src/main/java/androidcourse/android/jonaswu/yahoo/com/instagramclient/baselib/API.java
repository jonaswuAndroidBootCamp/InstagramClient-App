package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

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


    public interface DataHandler {
        public void onEventMainThread(API.ReturnDataEvent dma);
    }

    public static class ReturnDataEvent {
        public JSONObject data;
        public String path;

        public ReturnDataEvent(JSONObject data, String path) {
            this.data = data;
            this.path = path;
        }
    }

    public void getLastestCommentById(APIDelegator apiDelegator, String mediaId) {
        EventBus eventBus = apiDelegator.getEventBus();
        Context ctx = apiDelegator.getContext();
        HashMap<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put("access_token", ctx.getString(R.string.access_token));
        try {
            BaseAsyncTask task = new getDataUrlParams(ctx, String.format("v1/media/%s/comments", mediaId), urlParams, eventBus);
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
            BaseAsyncTask task = new getDataUrlParams(ctx, "v1/media/popular", urlParams, eventBus);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getDataUrlParams extends BaseAsyncTask {

        private String path;
        private HashMap<String, Object> data;

        public getDataUrlParams(Context context, String path, HashMap<String, Object> data, EventBus localBus) {
            super(context, localBus);
            this.path = path;
            this.data = data;
        }


        @Override
        public void my_doInBackground(Object... params) {
            try {
                ReturnDataEvent returnDataEvent;
                returnDataEvent = new ReturnDataEvent(WebClient.basicHTTPGet(context, data, this.path, localBus), this.path);
                Log.e("pre receive data", returnDataEvent.data.toString());
                localBus.post(returnDataEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
