package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.API;
import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.APIDelegator;
import androidcourse.android.jonaswu.yahoo.com.instagramclient.baselib.BaseFragmentActivity;


public class MainActivity extends BaseFragmentActivity implements APIDelegator {

    private SwipeRefreshLayout swipeContainer;
    private MainItemAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        listView = (ListView) findViewById(R.id.mainlist);
    }

    public void initData() {
        adapter = new MainItemAdapter(this);
        adapter.setPopCommentDiagDelegator(this.getEventBus());
        adapter.setPopVideoDiagDelegator(this.getEventBus());
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        new API().getPopular(this);

    }

    public void fetchTimelineAsync(int page) {
        new API().getPopular(this);
    }


    public void onEventMainThread(API.ReturnDataEvent dma) {
        if (dma.path == "v1/media/popular") {
            adapter.deleteAll();
            try {
                JSONArray data = dma.data.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject singleItem = data.getJSONObject(i);
                    adapter.addItem(singleItem);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            swipeContainer.setRefreshing(false);
        } else {
            try {
                JSONArray comments = dma.data.getJSONArray("data");
                setSupportProgressBarIndeterminateVisibility(false);
                showCommentsDialog(comments);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void onEventMainThread(PopCommentDiag dma) {
        JSONObject item = adapter.getItem(dma.position);
        try {
            // showEditDialog(item.getJSONObject("comments").getJSONArray("data"));
            String mediaId = item.getString("id");
            setSupportProgressBarIndeterminateVisibility(true);
            new API().getLastestCommentById(MainActivity.this, mediaId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEventMainThread(PopVideoDiag dma) {
        JSONObject item = adapter.getItem(dma.position);
        try {
            String url = item.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
            showVideoDialog(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showCommentsDialog(JSONArray data) {
        FragmentManager fm = getSupportFragmentManager();
        CommentsDialog commentsDialog = CommentsDialog.newInstance(data);
        commentsDialog.show(fm, "fragment_edit_name");
    }


    private void showVideoDialog(String url) {
        FragmentManager fm = getSupportFragmentManager();
        VideoFragment videoFragment = VideoFragment.newInstance(url);
        videoFragment.show(fm, "fragment_edit_name");
    }

    private void showCommentsDialog(String mediaId) {
        FragmentManager fm = getSupportFragmentManager();
        CommentsDialog commentsDialog = CommentsDialog.newInstance(mediaId);
        commentsDialog.show(fm, "fragment_edit_name");
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Created by jonaswu on 2015/7/29.
     */
    public static class PopCommentDiag {
        public int position;

        public PopCommentDiag(int position) {
            this.position = position;
        }

    }

    /**
     * Created by jonaswu on 2015/7/29.
     */
    public static class PopVideoDiag {
        public int position;

        public PopVideoDiag(int position) {
            this.position = position;
        }

    }
}
