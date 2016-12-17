package user.com.myqclvideodemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import user.com.myqclvideodemo.base.BaseViewHolder;
import user.com.myqclvideodemo.utils.ScreenUtil;

/**
 * Created by RaphetS on 2016/10/19.
 */

public class videoAdapter extends RecyclerView.Adapter {
    private List<Bean.V9LG4E6VRBean> mDatas;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private final int item_type = 100;
    private final int load_type = 101;
    private OnItemClickListener mItemClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading = false;
    private boolean isFirst=true;

    public videoAdapter(Context mContext, List<Bean.V9LG4E6VRBean> mDatas, RecyclerView mRecyclerView) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        initRcyclerView();
    }

    private void initRcyclerView() {
      /*  GridLayoutManager mLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mDatas.size()) {
                    return 2;
                } else {
                    return 1;
                }

            }
        });*/

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();
                if (lastVisibleItemPosition > itemCount - 2 && !isLoading && dy > 0) {
                    isLoading = true;
                    if (mOnLoadMoreListener!=null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == load_type) {
            View loadView = LayoutInflater.from(mContext).inflate(R.layout.load_view, parent, false);
            LoadViewHolder holder = new LoadViewHolder(loadView);
            return holder;

        } else {
            View girlView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
            BaseViewHolder holder = new BaseViewHolder(girlView);
            return holder;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof BaseViewHolder) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
            JCVideoPlayer jcVideoPlayer = baseViewHolder.getView(R.id.videoplayer);
            ViewGroup.LayoutParams layoutParams = jcVideoPlayer.getLayoutParams();
            layoutParams.height = ScreenUtil.getScreenHeight(mContext) / 3;
           jcVideoPlayer.setUp(mDatas.get(position).mp4_url,mDatas.get(position).cover,mDatas.get(position).title);
        }


    }


    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()) {
            return load_type;
        } else {
            return item_type;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public void loadCompleted() {
        isLoading = false;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    class LoadViewHolder extends RecyclerView.ViewHolder {
        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }

}
