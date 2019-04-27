package com.example.ocrv20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class caseHistoryListActivity extends AppCompatActivity {
    private String tag = "appTest";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_history_data_list);

        ListView case_list = findViewById(R.id.case_data_list);

        List<String> files = getFilesAllName(getExternalFilesDir("txt").toString()+"/");

        for(int i=0;i<files.size();i++){
            Log.i(tag,"文件："+files.get(i));
        }

    }

    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

}
