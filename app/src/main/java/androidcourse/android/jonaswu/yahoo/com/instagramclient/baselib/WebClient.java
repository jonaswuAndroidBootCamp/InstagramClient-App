package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/28.
 */

public abstract class WebClient {
    static CommandSender sender = null;
    static boolean isPostRequest;

    public static CommandSender getSender(Context context) {
        if (sender == null)
            sender = new CommandSender(context, Config.hostUrl);
        return sender;
    }

    public static JSONObject basicHTTPPostJSON(Context context, JSONObject obj, String apiurl, EventBus localBus)
            throws Exception {
        isPostRequest = true;
        sender = getSender(context);
        sender.setAction(apiurl, isPostRequest);
        HttpResponse httpResponse = sender.sendJSON(isPostRequest,
                obj.toString());
        return responseHandler(httpResponse);
    }

    public static JSONObject basicHTTPGet(
            Context context, HashMap<String, Object> data, String apiurl, EventBus localBus)
            throws Exception {
        isPostRequest = false;
        sender = getSender(context);
        sender.prepareNewNameValuePairs();
        sender.clearGetParams();
        if (data instanceof HashMap) {
            Iterator<?> it = data.entrySet().iterator();
            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                HashMap.Entry pairs = (HashMap.Entry) it.next();
                sender.addGetParams(pairs.getKey().toString(), pairs.getValue()
                        .toString());
            }
        }
        sender.setAction(apiurl, isPostRequest);
        HttpResponse httpResponse = sender.send(isPostRequest);
        return responseHandler(httpResponse);
    }

    private static JSONObject responseHandler(
            HttpResponse httpResponse) throws
            Exception {
        String result = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                result = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("excpetion!");
            }
        } else
            throw new Exception("exception");
        return new JSONObject(result);
    }
}
