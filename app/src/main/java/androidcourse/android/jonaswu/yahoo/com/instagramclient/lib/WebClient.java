package androidcourse.android.jonaswu.yahoo.com.instagramclient.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /**
     * Created by jonaswu on 2015/7/28.
     */

    public static class CommandSender {

        private String hostname;

        private HttpPost httpPost;
        private HttpGet httpGet;
        private HttpResponse httpResponse;
        private List<BasicNameValuePair> params;
        private HashMap<String, String> httpGetParams;
        private Context context;
        private DefaultHttpClient defaultHttpClient;

        public DefaultHttpClient getHttpClient() {
            return defaultHttpClient;
        }

        public CommandSender(Context ctx, String hostname) {
            this.context = ctx;
            this.hostname = hostname;
            httpPost = new HttpPost(hostname);
            httpPost.addHeader("Accept-Language", "zh-tw");
            httpPost.addHeader("Content-Type", "application/json");
            httpGet = new HttpGet(hostname);
            httpGet.addHeader("Accept-Language", "zh-tw");

            params = new ArrayList<BasicNameValuePair>();
            httpGetParams = new HashMap<String, String>();
            defaultHttpClient = new DefaultHttpClient();
        }

        public boolean prepareNewNameValuePairs() {
            this.params = new ArrayList<BasicNameValuePair>();
            return true;
        }

        /**
         * Add a pair of parameter
         *
         * @param name  Pair name
         * @param value Pair value
         */
        public void add(String name, String value) {
            params.add(new BasicNameValuePair(name, value));
        }

        public HttpResponse send(boolean isPostRequest) throws Exception {
            if (!haveNetworkConnection(this.context)) {
                throw new Exception("no network");
            }
            try {
                if (isPostRequest) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = defaultHttpClient.execute(httpPost);
                } else {
                    httpResponse = defaultHttpClient.execute(httpGet);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return httpResponse;
        }

        public HttpResponse sendJSON(boolean isPostRequest, String data)
                throws Exception {
            if (!haveNetworkConnection(this.context)) {
                throw new Exception("no network");
            }
            try {
                if (isPostRequest) {
                    httpPost.setEntity(new ByteArrayEntity(data.toString()
                            .getBytes("utf8")));
                    httpResponse = defaultHttpClient.execute(httpPost);
                } else {
                    httpResponse = defaultHttpClient.execute(httpGet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        public void setAction(String apiurl, boolean isPostRequest) {
            String temphostname = "";
            temphostname = apiurl;
            URI uri = null;
            Log.d("CommandSender - SetAction", "apiurl = " + apiurl);
            try {
                uri = new URI(hostname + temphostname);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (isPostRequest) {
                httpPost.setURI(uri);
                Log.d("CommandSender, isPostRequest", uri.toString());
            } else {
                String url = uri.toString();
                url = addParamIntoUrl(url);
                try {
                    Log.d("CommandSender", url);
                    httpGet.setURI(new URI(url));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        }

        public void setActionByDirectUrl(String apiurl, boolean isPostRequest) {
            String temphostname = "";
            temphostname = apiurl;
            URI uri = null;
            Log.d("CommandSender - setActionByDirectUrl", "apiurl = " + apiurl);
            try {
                uri = new URI(temphostname + '/');
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (isPostRequest) {
                httpPost.setURI(uri);
                Log.d("CommandSender - setActionByDirectUrl", uri.toString());
            } else {
                String url = uri.toString();
                url = addParamIntoUrl(url);
                try {
                    httpGet.setURI(new URI(url));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        }

        public void addGetParams(String name, String value) {
            httpGetParams.put(name, value);
        }

        public void clearGetParams() {
            if (httpGetParams != null) {
                Log.d("CommandSender - clearGetParams", "httpGetParams != null");
                httpGetParams.clear();
            } else
                Log.d("CommandSender - clearGetParams", "httpGetParams is null");
        }

        @SuppressWarnings("rawtypes")
        public String addParamIntoUrl(String url) {
            boolean isFirstEnter = true;
            Iterator it = httpGetParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();

                if (isFirstEnter) {
                    url += "?";
                    isFirstEnter = false;
                } else
                    url += "&";

                url += (key + "=" + URLEncoder.encode(val));
            }

            return url;
        }

        public static boolean haveNetworkConnection(Context context) {

            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) {
                        haveConnectedWifi = true;
                        System.out.println("WIFI CONNECTION AVAILABLE");
                    } else {
                        System.out.println("WIFI CONNECTION NOT AVAILABLE");
                    }
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) {
                        haveConnectedMobile = true;
                        System.out.println("MOBILE INTERNET CONNECTION AVAILABLE");
                    } else {
                        System.out
                                .println("MOBILE INTERNET CONNECTION NOT AVAILABLE");
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
    }
}
