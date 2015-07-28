package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.CustomizedAdapter;
import de.greenrobot.event.EventBus;

/**
 * Created by jonaswu on 2015/7/23.
 */
public class MainItemAdapter extends CustomizedAdapter {


    private static int MAX_COMMENT = 2;
    private EventBus popCommentDiagDelegator;
    private EventBus popVideoDiagDelegator;

    public MainItemAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.list_item, null);
        }
        JSONObject p = this.getItem(position);
        try {
            JSONObject user = p.getJSONObject("user");
            JSONArray comments = p.getJSONObject("comments").getJSONArray("data");
            TextView name = (TextView) v.findViewById(R.id.username);
            TextView likeCount = (TextView) v.findViewById(R.id.like_count);
            TextView commentCount = (TextView) v.findViewById(R.id.comments_count);
            RelativeLayout comment_section = (RelativeLayout) v.findViewById(R.id.comment_section);
            comment_section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainItemAdapter.this.popCommentDiagDelegator != null) {
                        MainItemAdapter.this.popCommentDiagDelegator.post(new MainActivity.PopCommentDiag(position));
                    }
                }
            });
            TextView time = (TextView) v.findViewById(R.id.time);
            // TextView comment = (TextView) v.findViewById(R.id.comment_text);
            ImageView userProfile = (ImageView) v.findViewById(R.id.user_profile);
            ImageView mainImage = (ImageView) v.findViewById(R.id.main_image);
            ImageView playOverlay = (ImageView) v.findViewById(R.id.play);

            try {
                p.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                playOverlay.setVisibility(View.VISIBLE);
                mainImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainItemAdapter.this.popVideoDiagDelegator != null) {
                            MainItemAdapter.this.popVideoDiagDelegator.post(new MainActivity.PopVideoDiag(position));
                        }
                    }
                });
            } catch (Exception e) {

            }

            LinearLayout latest_comments_container = (LinearLayout) v.findViewById(R.id.latest_comments_container);
            latest_comments_container.removeAllViews();


            LayoutInflater vi = LayoutInflater.from(context);

            for (int i = comments.length() - 1; i >= 0; i--) {
                if (MAX_COMMENT == comments.length() - 1 - i) {
                    break;
                }
                JSONObject commentObj = comments.getJSONObject(i);
                View latest_comment = vi.inflate(R.layout.latest_comment, null);
                TextView comment = (TextView) latest_comment.findViewById(R.id.comment_text);
                TextView comment_user = (TextView) latest_comment.findViewById(R.id.comments_user);
                comment.setText(commentObj.getString("text"));
                comment_user.setText(commentObj.getJSONObject("from").getString("username"));
                latest_comments_container.addView(latest_comment);

            }

            name.setText(user.getString("username"));
            likeCount.setText(p.getJSONObject("likes").getString("count"));
            commentCount.setText(p.getJSONObject("comments").getString("count"));
            // Picasso.with(context).load(p.getJSONObject("images").getJSONObject("standard_resolution").getString("url")).into(mainImage);
            Glide.with(context).load(user.getString("profile_picture")).into(userProfile);
            Glide.with(context)
                    .load(p.getJSONObject("images").getJSONObject("standard_resolution").getString("url"))
                            // .placeholder(R.drawable.spiffygif_30x30)
                    .into(mainImage);


            Date createTime = new Date(Long.valueOf(p.getString("created_time")) * 1000);
            Date currentTime = new Date();
            long hour = TimeUnit.MILLISECONDS.toHours(currentTime.getTime() - createTime.getTime());
            time.setText(String.valueOf(hour) + 'h');

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void setPopCommentDiagDelegator(EventBus popCommentDiagDelegator) {
        this.popCommentDiagDelegator = popCommentDiagDelegator;
    }

    public void setPopVideoDiagDelegator(EventBus popVideoDiagDelegator) {
        this.popVideoDiagDelegator = popVideoDiagDelegator;
    }
}
