package com.example.codercoral.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart, mEnd;
    public static String[] URLS;
    private boolean mFirstin;

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView) {
        mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        mFirstin = true;
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = data.get(i).newsIconURL;
        }
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHOlder viewHOlder;
        if (convertView == null) {
            viewHOlder = new ViewHOlder();
            convertView = mInflater.inflate(R.layout.news_item, parent, false);
            viewHOlder.ivIcon = convertView.findViewById(R.id.iv_icon);
            viewHOlder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewHOlder.tvContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHOlder);
        } else {
            viewHOlder = (ViewHOlder) convertView.getTag();
        }
        viewHOlder.ivIcon.setTag(mList.get(position).newsIconURL);
        /*        new ImageLoader().showImageByThread(viewHOlder.ivIcon, mList.get(position).newsIconURL);*/
        mImageLoader.showImageByAsyncTask(viewHOlder.ivIcon, mList.get(position).newsIconURL);
        viewHOlder.tvTitle.setText(mList.get(position).newsTitle);
        viewHOlder.tvContent.setText(mList.get(position).newsContent);
        return convertView;
    }

    //当滑动状态改变的时候会被调用。比如说由静止开始滑动，或者滑动停止的时候。
    //滑动停止时下载，加载图片
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //滑动停止时,加载可见项
            mImageLoader.loadImages(mStart, mEnd);
        } else {
            // 停止任务
            mImageLoader.cancelAllTasks();
        }
    }

    //onScroll()是一直处于被调用的状态。
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        //第一次显示时调用
        if (mFirstin && visibleItemCount > 0) {
            mImageLoader.loadImages(mStart, mEnd);
            mFirstin = false;
        }
    }

    class ViewHOlder {
        TextView tvTitle, tvContent;
        ImageView ivIcon;
    }
}
