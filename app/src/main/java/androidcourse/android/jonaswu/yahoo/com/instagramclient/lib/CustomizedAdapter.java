package androidcourse.android.jonaswu.yahoo.com.instagramclient.lib;

import android.content.Context;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jonaswu on 2015/7/23.
 */
public abstract class CustomizedAdapter extends BaseAdapter {

    protected ArrayList<JSONObject> listItem = new ArrayList<JSONObject>();
    protected Context context;

    public CustomizedAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setItem(int id, JSONObject value) {
        listItem.set(id, value);
        this.notifyDataSetChanged();
    }

    public void addItem(JSONObject value) {
        this.addItemWithoutNotifyChange(value);
        this.notifyDataSetChanged();
    }

    public void addItemWithoutNotifyChange(JSONObject value) {
        listItem.add(value);
    }


    public void deleteItem(int id) {
        listItem.remove(id);
        this.notifyDataSetChanged();
    }

    public void deleteAll() {
        listItem.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
