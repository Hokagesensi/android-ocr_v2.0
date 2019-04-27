package com.example.ocrv20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ocrHistoryDataListActivity extends AppCompatActivity {

    private List<bpItem> bpItems = new ArrayList<bpItem>();
    private ListView listView;
    private Button btn_ocr_delete_all;
    private Button btn_ocr_plot;
    private bpAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_history_data_list);

        listView = findViewById(R.id.ocr_data_list);
        btn_ocr_delete_all = findViewById(R.id.btn_ocrDataListDeleteAll);
        btn_ocr_plot = findViewById(R.id.btn_ocrDataPlot);
        searchData();
        adapter = new bpAdapter(ocrHistoryDataListActivity.this,R.layout.bp_item,bpItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] data = getData(position);
                Intent ocr_full_intent = new Intent(ocrHistoryDataListActivity.this,ocrFullShow.class);
                ocr_full_intent.putExtra("data",data);
                startActivityForResult(ocr_full_intent,1);
            }
        });

        //清空所有
        btn_ocr_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
                deleteDataAll();
            }
        });

        //绘制历史数据折线图
        btn_ocr_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("appTest:","开始绘制图像");
                String[] dataAll=getAllData();
                Log.i("appTest:","获取数据数量："+dataAll.length);
//                for(int i=0;i<dataAll.length;i++)
//                    for(int j=0;j<dataAll[i].length;j++)
//                        Log.i("appTest:",j+":"+dataAll[i][j]);

                Intent chart_intent = new Intent(ocrHistoryDataListActivity.this,
                        MultiLineChartActivity.class);
                chart_intent.putExtra("data",dataAll);
                startActivity(chart_intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            String result = data.getStringExtra("result");//得到新Activity 关闭后返回的数据
            Log.i("appTest:", "触发回调函数：" + result);
            if (result.equals("update")) {
                Log.i("appTest:", "清空listView");
                listView.setAdapter(null);
                bpItems.clear();
                searchData();
                adapter = new bpAdapter(ocrHistoryDataListActivity.this, R.layout.bp_item, bpItems);
                listView.setAdapter(adapter);
            }
        }
    }

    private void searchData() {
        String[] data = new String[3];
        DataBaseHelper dbHelper = new DataBaseHelper(ocrHistoryDataListActivity.this, "ocr_bp", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("bp", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            data[0] = cursor.getString(7); //time
            data[1] = cursor.getString(5); //bitmap uri
            data[2] = cursor.getString(1); //typeInfo
            Log.i("appTest:", "search data:" + data[0] + " " + data[1] + " " + data[2]);
            Uri imageUri = Uri.parse(data[1]);
            Bitmap bitmap = getBitmapFromUri(imageUri);
            bpItem bpItem = new bpItem(data[0], bitmap, data[2]);
            bpItems.add(bpItem);
        }
        cursor.close();
    }

    private String[] getData(int index){
        String[] data = new String[8];
        DataBaseHelper dbHelper = new DataBaseHelper(ocrHistoryDataListActivity.this, "ocr_bp", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("bp", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            cursor.move(index);
            for(int i=0;i<8;i++)
                data[i] = cursor.getString(i);
            Log.i("appTest: ", "database--->" + index + " "
                    + data[0] + " " + data[1] + " " + data[2] + " " + data[3] + " " + data[4] + " " +
                    data[5] + " " + data[6]+" "+data[7]);

        }
        cursor.close();
        return data;
    }

    private void deleteDataAll(){
        DocDataBaseHelper dbHelperdoc = new DocDataBaseHelper(ocrHistoryDataListActivity.this, "ocr_bp", null, 1);
        SQLiteDatabase dbdoc = dbHelperdoc.getWritableDatabase();
        dbdoc.execSQL("DELETE FROM bp");
    }

    private String[] getAllData(){
        int count = bpItems.size();
        String[] data = new String[count*4];
        String[] temp = new String[4];
        int dataCount=0;
        for(int i=0;i<count;i++){
            temp = getData(i);
            for(int j=0;j<3;j++){
                data[dataCount++]=temp[j+2];
            }
        }
        return data;
    }

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}