package com.example.ocrv20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class caseAdapter extends ArrayAdapter {
    private final int resourceId;

    public caseAdapter(Context context, int textViewResourceId, List<caseData> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        caseData bp = (caseData) getItem(position); // 获取当前项的case实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView casefilename = (TextView) view.findViewById(R.id.case_filename);//获取该布局内的文本视图
        casefilename.setText(bp.getFilename());//为文本视图设置文本内容
        return view;
    }
}
