package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.lib.API;
import de.greenrobot.event.EventBus;

public class CommentsFragment extends SherlockDialogFragment implements API.APIDelegator {

    private JSONArray comments;
    private CommentAdapter adapter;
    private ListView listView;
    private String mediaId;
    private EventBus eventBus;

    public CommentsFragment() {
        this.eventBus = new EventBus();
        this.eventBus.register(this);
    }

    public void setContentComment(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setContentComment(JSONArray comments) {
        this.comments = comments;
    }

    public static CommentsFragment newInstance(JSONArray comments) {
        CommentsFragment frag = new CommentsFragment();
        frag.setContentComment(comments);
        return frag;

    }

    public static CommentsFragment newInstance(String mediaId) {
        CommentsFragment frag = new CommentsFragment();
        frag.setContentComment(mediaId);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Empty constructor required for DialogFragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.comment_fragment, container);
        listView = (ListView) view.findViewById(R.id.commentlist);
        adapter = new CommentAdapter(getActivity());
        listView.setAdapter(adapter);
        if (this.comments != null) {
            setCommentsArrayAdapter();
        } else {
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            new API().getLastestCommentById(this, this.mediaId);
        }
        return view;
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public void setCommentsArrayAdapter() {
        for (int i = this.comments.length() - 1; i >= 0; i--) {
            try {
                adapter.addItem(this.comments.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onEventMainThread(API.ReturnDataEvent dma) {
        if (adapter != null) {
            try {
                this.comments = dma.data.getJSONArray("data");
                getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                setCommentsArrayAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
