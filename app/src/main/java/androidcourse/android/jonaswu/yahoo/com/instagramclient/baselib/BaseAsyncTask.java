package androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/28.
 */

public class BaseAsyncTask extends AsyncTask<Object, Object, Object> {
    protected Context context;
    protected EventBus localBus;
    protected ProgressDialog pd;

    public BaseAsyncTask(Context context, EventBus localBus) {
        this.context = context;
        this.localBus = localBus;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Object result) {
    }

    public void my_doInBackground(Object... params) {

    }

    public boolean isIdle() {
        return (this.getStatus() == AsyncTask.Status.FINISHED);
    }

    @Override
    protected Object doInBackground(Object... params) {
        // TODO Auto-generated method stub
        my_doInBackground(params);
        return null;
    }
}
