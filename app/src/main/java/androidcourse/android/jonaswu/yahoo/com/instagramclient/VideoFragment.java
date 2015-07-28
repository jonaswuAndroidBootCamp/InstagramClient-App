package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.API;
import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.APIDelegator;
import de.greenrobot.event.EventBus;

public class VideoFragment extends SherlockDialogFragment {

    private String url;
    private VideoView videoView;

    public VideoFragment() {
    }


    public static VideoFragment newInstance(String url) {
        VideoFragment frag = new VideoFragment();
        frag.setUrl(url);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container);
        videoView = (VideoView) view.findViewById(R.id.video);
        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(this.url);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();
        return view;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
