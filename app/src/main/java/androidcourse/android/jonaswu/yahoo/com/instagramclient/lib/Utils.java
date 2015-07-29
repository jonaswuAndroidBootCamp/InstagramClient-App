package androidcourse.android.jonaswu.yahoo.com.instagramclient.lib;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    public static JSONObject getAPIResponseDataAsJSON(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject.getJSONObject("data");
        } catch (Exception e) {
            Log.d("myLog", "Error parsing api.. response - " + response);
            return null;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
