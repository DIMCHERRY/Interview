package com.example.codercoral.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceId;
    public FruitAdapter( Context context, int resource,  List<Fruit> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    /**
     * listview中有多少个item，就会调用多少次getVIew
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view; /*= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);*/
        ViewHolder viewHolder;
        //将之前加载好的布局进行缓存
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = view.findViewById(R.id.fruit_image);
            viewHolder.fruitText = view.findViewById(R.id.Text_two);
            view.setTag(viewHolder);//viewHolder存储在view中,不需要每次都通过findViewById获取实例
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
/*        ImageView imageView = view.findViewById(R.id.fruit_image);
        TextView textView = view.findViewById(R.id.Text_two);*/
        viewHolder.fruitImage.setImageResource(fruit.getImageID());
        viewHolder.fruitText.setText(fruit.getName());
        return view;
    }

    private class ViewHolder {
        ImageView fruitImage;
        TextView fruitText;
    }
}
