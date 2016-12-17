package user.com.myqclvideodemo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSrl;
    private RecyclerView mRecyclerView;
    private List<Bean.V9LG4E6VRBean> list=new ArrayList<>();
    private Gson gson=new Gson();
    private videoAdapter mAdapter;
    private String url="http://c.3g.163.com/nc/video/list/V9LG4E6VR/n/1-50.html";
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSrl = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        initSwipeRefreshLayout();
        initRecyclerView();
        addListener();
        initData();
    }










    /**
     * 下拉刷新的初始化
     */
    private void initSwipeRefreshLayout() {
        mSrl.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "已是最新!！", Toast.LENGTH_SHORT).show();
                                if(mSrl.isRefreshing()) {
                                    mSrl.setRefreshing(false);
                                }
                            }
                        });
                    }

                },1000);
            }
        });
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new videoAdapter(this,list,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }
    /**
     * 添加监听事件
     */
    private void addListener() {

        mAdapter.setOnLoadMoreListener(new videoAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.loadCompleted();
                                Toast.makeText(MainActivity.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                },1000);

            }
        });

        mAdapter.setOnItemClickListener(new videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


    /**
     * 获得数据
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL urls = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if(conn.getResponseCode()==200){
                        InputStream is = conn.getInputStream();
                        String json = streamToString(is);
                        Bean bean = gson.fromJson(json, Bean.class);
                        list.addAll(bean.V9LG4E6VR);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //流转换成字符串
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new  ByteArrayOutputStream();

            byte[] buffer = new  byte[1024];

            int  len = 0;

            while ((len = is.read(buffer)) != -1){
                baos.write(buffer, 0,  len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        }
        catch (Exception e) {
            return null;
        }

    }



}
