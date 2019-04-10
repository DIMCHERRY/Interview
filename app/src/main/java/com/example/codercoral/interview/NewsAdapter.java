package com.example.codercoral.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private List<NewsBean> mList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context, List<NewsBean> data){
        mList = data;
        mInflater = LayoutInflater.from(context);
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
        ViewHOlder viewHOlder = null;
        if(convertView == null){
            viewHOlder = new ViewHOlder();
            convertView = mInflater.inflate(R.layout.news_item, null);
            viewHOlder.ivIcon = convertView.findViewById(R.id.iv_icon);
            viewHOlder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewHOlder.tvContent = convertView.findViewById(R.id.tv_content);
        }else{
            viewHOlder = (ViewHOlder) convertView.getTag();
        }
        viewHOlder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHOlder.tvTitle.setText(mList.get(position).newsTitle);
        viewHOlder.tvContent.setText(mList.get(position).newsContent);
        return convertView;
    }

    class ViewHOlder{
        public TextView tvTitle, tvContent;
        public ImageView ivIcon;
    }
}
