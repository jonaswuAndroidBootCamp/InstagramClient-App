package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.CustomizedAdapter;

/**
 * Created by jonaswu on 2015/7/23.
 */
public class CommentAdapter extends CustomizedAdapter {


    public CommentAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.context);
            v = vi.inflate(R.layout.comment_item, null);
        }
        JSONObject p = this.getItem(position);
        try {
            JSONObject user = p.getJSONObject("from");
            TextView comment_text = (TextView) v.findViewById(R.id.comment_text);
            TextView username = (TextView) v.findViewById(R.id.username);
            TextView time = (TextView) v.findViewById(R.id.time);
            ImageView profile_picture = (ImageView) v.findViewById(R.id.user_profile);
            comment_text.setText(p.getString("text"));
            username.setText(user.getString("username"));
            Glide.with(context)
                    .load(user.getString("profile_picture"))
                    .into(profile_picture);

            Date createTime = new Date(Long.valueOf(p.getString("created_time")) * 1000);
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setText(form.format(createTime));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
