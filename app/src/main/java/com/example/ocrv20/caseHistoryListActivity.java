package com.example.ocrv20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class caseHistoryListActivity extends AppCompatActivity {
    private String tag = "appTest";
    private caseAdapter adapter;
    private List<caseData> caseItems=new ArrayList<caseData>();
    private String filepath;
    private ListView case_list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_history_data_list);

        case_list = findViewById(R.id.case_data_list);
        filepath = getExternalFilesDir("txt").toString()+"/";
        caseItems=searchFiles(filepath);

        adapter = new caseAdapter(caseHistoryListActivity.this,R.layout.case_item,caseItems);
        case_list.setAdapter(adapter);

        case_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filepath = caseItems.get(position).getFilepath();
                Log.i(tag,"选中文件路径为："+filepath);
                Intent caseExtract_intent = new Intent(caseHistoryListActivity.this,caseHistoryDataActivity.class);
                caseExtract_intent.putExtra("filepath",filepath);
                startActivity(caseExtract_intent);
            }
        });

    }

    public static List<caseData> searchFiles(String path){
        List<caseData> caseItems=new ArrayList<caseData>();
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            caseData caseItem=new caseData(files[i].getAbsolutePath());
            caseItems.add(caseItem);
        }
        return caseItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            String result = data.getStringExtra("result");//得到新Activity 关闭后返回的数据
            Log.i("appTest:", "触发回调函数：" + result);
            if (result.equals("update")) {
                Log.i("appTest:", "清空listView");
                case_list.setAdapter(null);
                caseItems.clear();
                caseItems=searchFiles(filepath);
                adapter = new caseAdapter(caseHistoryListActivity.this, R.layout.case_item, caseItems);
                case_list.setAdapter(adapter);
            }
        }
    }

}
