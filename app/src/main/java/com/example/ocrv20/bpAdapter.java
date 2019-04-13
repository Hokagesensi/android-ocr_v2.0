package com.example.ocrv20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class bpAdapter extends ArrayAdapter {
    private final int resourceId;

    public bpAdapter(Context context, int textViewResourceId, List<bpItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        bpItem bp = (bpItem) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView ocrImage = (ImageView) view.findViewById(R.id.bp_image);//获取该布局内的图片视图
        TextView ocrTime = (TextView) view.findViewById(R.id.bp_time);//获取该布局内的文本视图
        TextView ocrType = (TextView) view.findViewById(R.id.bp_type);
        ocrImage.setImageBitmap(bp.getBitmap());//为图片视图设置图片资源
        ocrTime.setText(bp.getTime());//为文本视图设置文本内容
        ocrType.setText(bp.getTypeinfo());//设置类型
        return view;
    }


}
